package com.dlfsystems.landlord.screens.propview

import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter

class PropviewPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    fun state() = stateHolder.state.value as PropviewState

    val repo = FirebaseRepository()

    override fun hearAction(action: Action) {

    }
}