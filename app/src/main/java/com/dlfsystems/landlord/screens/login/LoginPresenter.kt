package com.dlfsystems.landlord.screens.login

import com.dlfsystems.landlord.MainActivity
import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.data.model.User
import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter
import com.dlfsystems.landlord.screens.proplist.ProplistKey
import java.lang.RuntimeException

class LoginPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    fun state() = stateHolder.state.value as LoginState

    val repo = FirebaseRepository()

    override fun hearAction(action: Action) {
        when {
            (action is InitialState) -> { }
            (action is LogIn) -> {
                mutate(state().copy(lastError = "", waiting = true))
                loginUser(action.username, action.password)
            }
            (action is Register) -> {
                mutate(state().copy(lastError = "", waiting = true))
                registerUser(action.username, action.password)
            }
            else -> { throw RuntimeException(action.toString()) }
        }
    }


    private fun loginUser(username: String, password: String) {
        Rudder.auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener {
                if (it.isSuccessful()) {
                    val uid = Rudder.auth.currentUser!!.uid
                    repo.getUser(uid) {
                        onLogin(it!!, password)
                    }
                } else {
                    mutate(state().copy(lastError = it.exception?.message ?: "Couldn't log in -- try again.",
                                        waiting = false))
                }
            }
    }

    private fun registerUser(username: String, password: String) {
        Rudder.auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener {
                if (it.isSuccessful()) {
                    val uid = Rudder.auth.currentUser!!.uid
                    val user = User(uid = uid, username = username)
                    repo.putUser(user)
                    onLogin(user, password)
                } else {
                    mutate(state().copy(lastError = it.exception?.message ?: "Couldn't register -- try again.",
                                        waiting = false))
                }
            }
    }

    fun onLogin(user: User, password: String) {
        (fragment.activity as MainActivity).onLogin(user, password)
        mutate(state().copy(lastError = "", waiting = false))
        Rudder.navTo(ProplistKey())
    }
}