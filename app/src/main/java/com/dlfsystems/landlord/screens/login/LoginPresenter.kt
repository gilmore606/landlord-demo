package com.dlfsystems.landlord.screens.login

import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BasePresenter
import timber.log.Timber
import java.lang.RuntimeException

class LoginPresenter : BasePresenter() {

    fun state() = stateHolder.state.value as LoginState

    override fun hearAction(action: Action) {
        when {
            (action is LoginAction) -> {
                mutate(state().copy(waiting = true))
                loginUser(action.username, action.password)
            }
            (action is RegisterAction) -> {
                mutate(state().copy(waiting = true))
                registerUser(action.username, action.password)
            }
            else -> { throw RuntimeException(action.toString()) }
        }
    }


    private fun loginUser(username: String, password: String) {
        Rudder.auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener {
                if (it.isSuccessful()) {
                    mutate(state().copy(waiting = false))
                    Timber.d("Login success")
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
                    mutate(state().copy(waiting = false))
                    Timber.d("Registration success")
                } else {
                    mutate(state().copy(lastError = it.exception?.message ?: "Couldn't register -- try again.",
                                        waiting = false))
                }
            }
    }
}