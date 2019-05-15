package com.dlfsystems.landlord.screens.propdetail

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.data.model.User
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState
import com.dlfsystems.landlord.validate
import kotlinx.android.synthetic.main.fragment_propdetail.*
import timber.log.Timber

class PropdetailFragment : BaseFragment() {

    override val layoutResource = R.layout.fragment_propdetail
    override val presenter = PropdetailPresenter(this)
    override fun defaultState() = PropdetailState()

    fun state() = stateHolder.state.value as PropdetailState

    val repo = FirebaseRepository()

    override fun makeStateFromArguments(arguments: Bundle): BaseState =
            PropdetailState(
                propId = arguments.getSerializable("propId") as String
            )

    override fun subscribeUI(view: View) {

        var realtorList = listOf<String>()
        var realtorIds = listOf<String>()

        repo.getRealtors {
            realtorList = it.map { it.username }
            realtorIds = it.map { it.uid }
            val realtorAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, realtorList)
            realtorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            propdetail_realtor_spinner.adapter = realtorAdapter
            Timber.d("REPO realtors " + realtorList.toString())
        }


        propdetail_realtor_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(arg0: AdapterView<*>) { }
            override fun onItemSelected(arg0: AdapterView<*>, view: View, position: Int, id: Long) {
                actions.onNext(SelectRealtor(realtorIds[position]))
            }
        }

        propdetail_rooms.validate({ it.toIntOrNull() ?: 0 > 0 }, "Number of rooms")

        propdetail_address_gps_button.setOnClickListener {
            actions.onNext(LocateAddress())
        }
        propdetail_address_coords_button.setOnClickListener {
            actions.onNext(LocateAddressFromCoords(propdetail_coordx.text.toString().toDouble(),
                propdetail_coordy.text.toString().toDouble()))
        }
        propdetail_coords_gps_button.setOnClickListener {
            actions.onNext(LocateCoords())
        }
        propdetail_coords_address_button.setOnClickListener {
            actions.onNext(LocateCoordsFromAddress(propdetail_address.text.toString()))
        }
        propdetail_submit_button.setOnClickListener {
            actions.onNext(SubmitProp(propFromState()))
        }
    }

    override fun render(state: BaseState) {
        state as PropdetailState

        if (!state.loading) {
            propdetail_loader.visibility = View.GONE
            propdetail_content.visibility = View.VISIBLE

            propdetail_coordx.setText(state.coordx.toString())
            propdetail_coordy.setText(state.coordy.toString())
            propdetail_address.setText(state.address)
            propdetail_city.setText(state.city)
            propdetail_state.setText(state.state)
            propdetail_zip.setText(state.zip)
        } else {
            propdetail_loader.visibility = View.VISIBLE
            propdetail_content.visibility = View.GONE
        }
    }

    private fun propFromState(): Prop {
        var prop = Prop(
            id = state().propId,
            coordx = propdetail_coordx.text.toString().toDouble(),
            coordy = propdetail_coordy.text.toString().toDouble(),
            name = propdetail_name.text.toString(),
            desc = propdetail_desc.text.toString(),
            sqft = propdetail_sqft.text.toString().toInt(),
            rent = propdetail_rent.text.toString().toInt(),
            rooms = propdetail_rooms.text.toString().toInt(),
            addtime = System.currentTimeMillis(),
            available = true,
            realtorId = state().realtorId,
            address = propdetail_address.text.toString(),
            city = propdetail_city.text.toString(),
            state = propdetail_state.text.toString(),
            zip = propdetail_zip.text.toString()
        )
        return prop
    }
}