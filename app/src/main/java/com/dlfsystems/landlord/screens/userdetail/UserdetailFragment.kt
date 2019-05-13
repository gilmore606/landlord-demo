package com.dlfsystems.landlord.screens.userdetail

import android.os.Bundle
import android.view.View
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.data.model.User
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState
import kotlinx.android.synthetic.main.fragment_userdetail.*

class UserdetailFragment : BaseFragment() {

    override val layoutResource = R.layout.fragment_userdetail
    override val presenter = UserdetailPresenter(this)
    override fun defaultState() = UserdetailState()

    fun state() = stateHolder.state.value as UserdetailState

    override fun makeStateFromArguments(arguments: Bundle): BaseState =
        UserdetailState(
            userId = arguments.getSerializable("userId") as String
        )

    override fun subscribeUI(view: View) {
        userdetail_realtor_switch.setOnCheckedChangeListener { _, value ->
            actions.onNext(UserChangeRealtor(value))
        }
        userdetail_admin_switch.setOnCheckedChangeListener { _, value ->
            actions.onNext(UserChangeAdmin(value))
        }
        userdetail_apply_button.setOnClickListener {
            actions.onNext(UserSaveChanges(userFromState()))
        }
        userdetail_delete_button.setOnClickListener {
            actions.onNext(UserDelete(userFromState()))
        }
    }

    private fun userFromState(): User {
        val state = state()
        return User(
            uid = state.userId,
            username = state.username,
            isRealtor = state.isRealtor,
            isAdmin = state.isAdmin
        )
    }

    override fun render(state: BaseState) {
        state as UserdetailState

        when {
            (state.loading) -> {
                userdetail_content.visibility = View.GONE
                userdetail_loader.visibility = View.VISIBLE
            }
            (!state.loading and (state.username == "")) -> {
                actions.onNext(LoadUser(state.userId))
            }
            (!state.loading and (state.username != "")) -> {
                userdetail_loader.visibility = View.GONE
                userdetail_content.visibility = View.VISIBLE

                userdetail_username.setText(state.username)
                userdetail_realtor_switch.isChecked = state.isRealtor
                userdetail_admin_switch.isChecked = state.isAdmin
            }
        }
    }
}