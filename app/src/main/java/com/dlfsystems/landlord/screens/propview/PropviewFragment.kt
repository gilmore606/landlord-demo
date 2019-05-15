package com.dlfsystems.landlord.screens.propview

import android.os.Bundle
import android.view.View
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState

class PropviewFragment : BaseFragment() {

    override val layoutResource = R.layout.fragment_propview
    override val presenter = PropviewPresenter(this)
    override fun defaultState() = PropviewState()

    fun state() = stateHolder.state.value as PropviewState

    override fun makeStateFromArguments(arguments: Bundle): BaseState =
            PropviewState(
                propId = arguments.getSerializable("propId") as String
            )

    override fun subscribeUI(view: View) {

    }

    override fun render(state: BaseState) {
        state as PropviewState


    }
}