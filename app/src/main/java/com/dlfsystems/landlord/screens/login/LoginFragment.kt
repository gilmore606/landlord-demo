package com.dlfsystems.landlord.screens.login

import android.view.View
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.plusAssign
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    override val layoutResource = R.layout.fragment_login
    override val presenter = LoginPresenter()
    override fun defaultState() = LoginState()

    fun state() = stateHolder.state.value as LoginState

    override fun subscribeUI(view: View) {
        disposables += login_username.textChanges().subscribe {
                stateHolder.mutate(state().copy(
                    username = it.toString()
                ))
            }

        disposables += login_password.textChanges().subscribe {
                stateHolder.mutate(state().copy(
                    password = it.toString()
                ))
            }

        disposables += login_button_login.clicks().subscribe {
                actions.onNext(LoginAction(
                    state().username,
                    state().password
                ))
            }

        disposables += login_button_register.clicks().subscribe {
                actions.onNext(RegisterAction(
                    state().username,
                    state().password
                ))
            }
    }

    override fun render(state: BaseState) {
        state as LoginState

        val canSubmit = !state.waiting and (state.username != "") and (state.password != "")

        login_button_login.visibility =
            if (canSubmit) View.VISIBLE else View.INVISIBLE
        login_button_register.visibility =
            if (canSubmit) View.VISIBLE else View.INVISIBLE

        login_errortext.text = state.lastError
    }
}