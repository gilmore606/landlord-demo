package com.dlfsystems.landlord.screens.propdetail

import android.os.Bundle
import android.view.View
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState
import kotlinx.android.synthetic.main.fragment_propdetail.*

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
        propdetail_address_gps_button.setOnClickListener {
            actions.onNext(LocateAddress())
        }
        propdetail_coords_gps_button.setOnClickListener {
            actions.onNext(LocateCoords())
        }
        propdetail_coords_address_button.setOnClickListener {
            actions.onNext(LocateCoordsFromAddress(propdetail_address.text.toString()))
        }
    }

    override fun render(state: BaseState) {
        state as PropdetailState

        if (!state.loading) {
            propdetail_coordx.setText(state.coordx.toString())
            propdetail_coordy.setText(state.coordy.toString())
        }
    }
}