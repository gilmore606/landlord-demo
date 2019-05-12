package com.dlfsystems.landlord.screens.login

import com.dlfsystems.landlord.Prefs
import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.data.model.User
import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter
import com.dlfsystems.landlord.screens.proplist.ProplistKey
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber
import java.lang.RuntimeException

class LoginPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    fun state() = stateHolder.state.value as LoginState

    override fun hearAction(action: Action) {
        when {
            (action is LoginAction) -> {
                mutate(state().copy(lastError = "", waiting = true))
                loginUser(action.username, action.password)
            }
            (action is RegisterAction) -> {
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
                    Prefs(fragment.context!!).loginUser = username
                    Prefs(fragment.context!!).loginPassword = password
                    Prefs(fragment.context!!).userId = Rudder.auth.currentUser!!.uid
                    mutate(state().copy(lastError = "", waiting = false))
                    Rudder.navTo(ProplistKey())
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
                    FirebaseRepository().putUser(User(username = username))
                    loginUser(username, password)
                } else {
                    mutate(state().copy(lastError = it.exception?.message ?: "Couldn't register -- try again.",
                                        waiting = false))
                }
            }
    }
}