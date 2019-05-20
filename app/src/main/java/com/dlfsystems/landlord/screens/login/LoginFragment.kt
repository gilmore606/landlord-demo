package com.dlfsystems.landlord.screens.login

import android.view.View
import com.dlfsystems.landlord.*
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    val prefs by lazy { Prefs(context!!) }

    override val showToolbar = false
    override val layoutResource = R.layout.fragment_login
    override val presenter = LoginPresenter(this)
    override fun defaultState() = LoginState(
        username = prefs.loginUser,
        password = prefs.loginPassword
    )

    fun state() = stateHolder.state.value as LoginState

    override fun subscribeUI(view: View) {

        login_username.validate({ it.isNotEmpty() }, "your email address", {
            stateHolder.mutate(state().copy(username = it.trim { it == ' '}))
        })
        login_password.validate({ it.length > 5 }, "your password", {
            stateHolder.mutate(state().copy(password = it))
        })

        login_button_login.setOnClickListener {
            actions.onNext(LogIn(
                state().username,
                state().password
            ))
        }
        login_button_register.setOnClickListener {
            actions.onNext(Register(
                state().username,
                state().password
            ))
        }
    }

    override fun render(state: BaseState) {
        state as LoginState

        val canSubmit = !state.waiting and (state.username != "") and (state.password != "")

        login_username.setIfChanged(state.username)
        login_password.setIfChanged(state.password)

        login_button_login.visibility =
            if (canSubmit) View.VISIBLE else View.GONE
        login_button_register.visibility =
            if (canSubmit) View.VISIBLE else View.GONE
        login_loader.visibility =
            if (state.waiting) View.VISIBLE else View.GONE

        login_errortext.text = state.lastError
    }
}