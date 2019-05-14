package com.dlfsystems.landlord.screens.propdetail

import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter

class PropdetailPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    fun state() = stateHolder.state.value as PropdetailState

    val repo = FirebaseRepository()

    override fun hearAction(action: Action) {


    }
}