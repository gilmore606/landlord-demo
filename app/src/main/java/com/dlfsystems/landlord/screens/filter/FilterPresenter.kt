package com.dlfsystems.landlord.screens.filter

import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter

class FilterPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    fun state() = stateHolder.state.value as FilterState

    override fun hearAction(action: Action) {

    }
}