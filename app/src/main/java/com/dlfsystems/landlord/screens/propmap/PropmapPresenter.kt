package com.dlfsystems.landlord.screens.propmap

import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter

class PropmapPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    fun state() = stateHolder.state.value as PropmapState

    override fun hearAction(action: Action) {
        when {
            (action is ChangeFilter) -> {
                mutate(state().copy(filter = action.filter))
            }
            else -> { throw RuntimeException(action.toString()) }
        }
    }
}