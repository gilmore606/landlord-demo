package com.dlfsystems.landlord.screens.propdetail

import android.os.Bundle
import android.view.View
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState

class PropdetailFragment : BaseFragment() {

    override val layoutResource = R.layout.fragment_propdetail
    override val presenter = PropdetailPresenter(this)
    override fun defaultState() = PropdetailState()

    fun state() = stateHolder.state.value as PropdetailState

    override fun makeStateFromArguments(arguments: Bundle): BaseState =
            PropdetailState(
                propId = arguments.getSerializable("propId") as String
            )

    override fun subscribeUI(view: View) {

    }

    override fun render(state: BaseState) {
        state as PropdetailState

    }
}