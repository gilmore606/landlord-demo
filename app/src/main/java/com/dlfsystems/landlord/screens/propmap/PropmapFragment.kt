package com.dlfsystems.landlord.screens.propmap

import android.view.View
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState

class PropmapFragment : BaseFragment() {

    override val layoutResource = R.layout.fragment_propmap
    override val presenter = PropmapPresenter(this)
    override fun defaultState() = PropmapState()

    fun state() = stateHolder.state.value as PropmapState

    override fun subscribeUI(view: View) {

    }

    override fun render(state: BaseState) {

    }
}