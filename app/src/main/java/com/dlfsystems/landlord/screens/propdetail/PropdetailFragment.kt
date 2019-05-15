package com.dlfsystems.landlord.screens.propdetail

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.isValidCoord
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState
import com.dlfsystems.landlord.setIfChanged
import com.dlfsystems.landlord.validate
import kotlinx.android.synthetic.main.fragment_propdetail.*
import kotlinx.android.synthetic.main.fragment_propdetail.view.*
import timber.log.Timber

class PropdetailFragment : BaseFragment() {

    override val layoutResource = R.layout.fragment_propdetail
    override val presenter = PropdetailPresenter(this)
    override fun defaultState() = PropdetailState()

    fun state() = stateHolder.state.value as PropdetailState

    val repo = FirebaseRepository()

    override fun makeStateFromArguments(arguments: Bundle): BaseState {
        val propId = arguments.getSerializable("propId") as String
        val loading = (propId != "")
        return PropdetailState(propId = propId, loading = loading)
    }

    override fun subscribeUI(view: View) {
        if (state().loading) {
            repo.getProp(state().propId) {
                actions.onNext(LoadProperty(it))
            }
            propdetail_submit_button.text = "UPDATE LISTING"
        }

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

        propdetail_name.validate({ it.length > 0 }, "Title of listing", {
            stateHolder.mutate(state().copy(name = it))
        })
        propdetail_desc.validate({ it.length > 0 }, "Description", {
            stateHolder.mutate(state().copy(desc = it))
        })
        propdetail_coordx.validate({ it.isValidCoord() }, "Latitude", {
            stateHolder.mutate(state().copy(coordx = it.toDouble()))
        })
        propdetail_coordy.validate({ it.isValidCoord() }, "Longitude", {
            stateHolder.mutate(state().copy(coordy = it.toDouble()))
        })
        propdetail_rooms.validate({ it.toIntOrNull() ?: 0 > 0 }, "Number of rooms", {
            stateHolder.mutate(state().copy(rooms = it.toInt()))
        })
        propdetail_sqft.validate({ it.toIntOrNull() ?: 0 > 0}, "Floor space", {
            stateHolder.mutate(state().copy(sqft = it.toInt()))
        })
        propdetail_rent.validate({ it.toIntOrNull() ?: 0 > 0}, "Monthly rent", {
            stateHolder.mutate(state().copy(rent = it.toInt()))
        })
        propdetail_address.validate({ it.length > 0 }, "Address", {
            stateHolder.mutate(state().copy(address = it))
        })
        propdetail_city.validate({ it.length > 0 }, "City", {
            stateHolder.mutate(state().copy(city = it))
        })
        propdetail_state.validate({ it.length == 2 }, "State code", {
            stateHolder.mutate(state().copy(state = it))
        })
        propdetail_zip.validate({ (it.length == 5) and (it.toIntOrNull() ?: 0 > 0) }, "5-digit ZIP code", {
            stateHolder.mutate(state().copy(zip = it))
        })

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
            if (isSubmittable())
                actions.onNext(SubmitProperty(propFromState()))
        }
    }

    override fun render(state: BaseState) {
        state as PropdetailState

        if (!state.loading) {
            propdetail_loader.visibility = View.GONE
            propdetail_content.visibility = View.VISIBLE

            propdetail_coordx.setIfChanged(state.coordx.toString())
            propdetail_coordy.setIfChanged(state.coordy.toString())
            propdetail_name.setIfChanged(state.name)
            propdetail_desc.setIfChanged(state.desc)
            propdetail_sqft.setIfChanged(state.sqft.toString())
            propdetail_rent.setIfChanged(state.rent.toString())
            propdetail_rooms.setIfChanged(state.rooms.toString())
            propdetail_address.setIfChanged(state.address)
            propdetail_city.setIfChanged(state.city)
            propdetail_state.setIfChanged(state.state)
            propdetail_zip.setIfChanged(state.zip)

            propdetail_submit_button.isEnabled = isSubmittable()
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

    private fun isSubmittable(): Boolean {
        return ((propdetail_coordx.error == null) and (propdetail_coordy.error == null) and
                (propdetail_name.error == null) and (propdetail_desc.error == null) and
                (propdetail_sqft.error == null) and (propdetail_rent.error == null) and
                (propdetail_rooms.error == null) and (propdetail_address.error == null) and
                (propdetail_city.error == null) and (propdetail_state.error == null) and
                (propdetail_zip.error == null))
    }
}