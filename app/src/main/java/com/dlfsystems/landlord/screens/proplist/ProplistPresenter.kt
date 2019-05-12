package com.dlfsystems.landlord.screens.proplist

import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BasePresenter

class ProplistPresenter : BasePresenter() {

    fun state() = stateHolder.state.value as ProplistState

    override fun hearAction(action: Action) {

    }
}