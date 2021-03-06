package com.dlfsystems.landlord.screens.propdetail

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dlfsystems.landlord.*
import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState
import kotlinx.android.synthetic.main.fragment_propdetail.*

class PropdetailFragment : BaseFragment() {

    override val layoutResource = R.layout.fragment_propdetail
    override val presenter = PropdetailPresenter(this)
    override fun defaultState() = PropdetailState()

    fun state() = stateHolder.state.value as PropdetailState

    val repo = FirebaseRepository()

    var realtorAdapter: ArrayAdapter<String>? = null

    override fun makeStateFromArguments(arguments: Bundle): BaseState {
        val propId = arguments.getSerializable("propId") as String
        val isNew = (propId == "")
        return PropdetailState(propId = propId, loaded = isNew, isNew = isNew)
    }

    override fun subscribeUI(view: View) {

        propdetail_realtor_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(arg0: AdapterView<*>) { }
            override fun onItemSelected(arg0: AdapterView<*>, view: View, position: Int, id: Long) {
                val state = state()
                state.realtorIds?.also {
                    state.realtorList?.also {
                        actions.onNext(SelectRealtor(state.realtorIds[position], state.realtorList[position]))
                    }
                }
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
        propdetail_rooms.validateNumeric({ it.toIntOrZero() > 0 }, "Number of rooms", {
            stateHolder.mutate(state().copy(rooms = it.toInt()))
        })
        propdetail_sqft.validateNumeric({ it.toIntOrZero() > 0 }, "Floor space", {
            stateHolder.mutate(state().copy(sqft = it.toInt()))
        })
        propdetail_rent.validateNumeric({ it.toIntOrZero() > 0 }, "Monthly rent", {
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
        propdetail_zip.validateNumeric({ (it.length == 5) and (it.toIntOrZero() > 0) }, "5-digit ZIP code", {
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
                actions.onNext(SubmitProperty(propFromState(state())))
        }
    }

    override fun render(state: BaseState) {
        state as PropdetailState

        propdetail_submit_button.setIfChanged(
            if (state.isNew) "CREATE LISTING" else "UPDATE LISTING"
        )

        if (state.loading)  {
            propdetail_loader.visibility = View.VISIBLE
            propdetail_content.visibility = View.GONE
        } else {
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

            context?.also { state.realtorList?.also {
                if (realtorAdapter == null) {
                    realtorAdapter = ArrayAdapter(context!!, R.layout.spinner_item, state.realtorList)
                    realtorAdapter!!.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    propdetail_realtor_spinner.adapter = realtorAdapter!!
                }
                state.realtorIds?.also {
                    propdetail_realtor_spinner.setIfChanged(state.realtorIds.indexOf(state.realtorId))
                }
            }}

            propdetail_submit_button.isEnabled = isSubmittable()
        }
    }

    override fun onPermissionGranted() = presenter.onPermissionGranted()

    private fun propFromState(state: PropdetailState): Prop {
        var prop = Prop(
            id = state.propId,
            coordx = state.coordx,
            coordy = state.coordy,
            name = state.name,
            desc = state.desc,
            sqft = state.sqft,
            rent = state.rent,
            rooms = state.rooms,
            addtime = if (state.addtime.toInt() == 0) System.currentTimeMillis() else state.addtime,
            available = state.available,
            realtorId = state.realtorId,
            address = state.address,
            city = state.city,
            state = state.state,
            zip = state.zip
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