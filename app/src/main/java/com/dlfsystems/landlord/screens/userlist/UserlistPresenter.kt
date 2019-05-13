package com.dlfsystems.landlord.screens.userlist

import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter
import com.dlfsystems.landlord.screens.userdetail.UserdetailKey

class UserlistPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    fun state() = stateHolder.state.value as UserlistState

    override fun hearAction(action: Action) {
        when {
            (action is ViewUserAction) -> {
                Rudder.navTo(UserdetailKey(action.userid))
            }
            else -> { throw RuntimeException(action.toString()) }
        }
    }
}