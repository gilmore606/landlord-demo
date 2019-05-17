package com.dlfsystems.landlord.screens.proplist

import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter
import com.dlfsystems.landlord.screens.propview.PropviewKey
import java.lang.RuntimeException

class ProplistPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    fun state() = stateHolder.state.value as ProplistState

    override fun hearAction(action: Action) {
        when {
            (action is ViewProperty) -> {
                Rudder.navTo(PropviewKey(action.propId))
            }
            (action is ChangeFilter) -> {
                mutate(state().copy(filter = action.filter))
            }
            else -> { throw RuntimeException(action.toString()) }
        }
    }
}