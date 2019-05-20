package com.dlfsystems.landlord.screens.propdetail

import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.dlfsystems.landlord.MainActivity
import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.ioThread
import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter
import com.google.android.gms.location.LocationServices
import java.lang.RuntimeException
import java.util.*

class PropdetailPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    fun state() = stateHolder.state.value as PropdetailState

    val repo = FirebaseRepository()
    val geocoder: Geocoder by lazy { Geocoder(fragment.activity, Locale.getDefault()) }

    var lastPermissionAction: Action? = null

    override fun hearAction(action: Action) {
        when {
            (action is InitialState) -> {
                val state = state()
                if (!state.loaded) {
                    mutate(state().copy(loading = true))
                    repo.getProp(state.propId) {
                        actions.onNext(LoadProperty(it))
                    }
                }
                if (state.realtorList == null) {
                    repo.getRealtors {
                        mutate(state().copy(
                            realtorList = it.map { it.username },
                            realtorIds = it.map { it.uid },
                            realtorUsername = (it.firstOrNull { it.uid == state().realtorId })?.username ?: state().realtorUsername
                        ))
                    }
                }
            }
            (action is LocateAddress) -> {
                lastPermissionAction = action
                mutate(state().copy(loading = true))
                requestCoordsHere {
                    mutate(state().copy(coordx = it.latitude,
                                        coordy = it.longitude))
                    requestAddress(it.latitude, it.longitude) {
                        updateAddress(it)
                    }
                }
            }
            (action is LocateCoords) -> {
                lastPermissionAction = action
                mutate(state().copy(loading = true))
                requestCoordsHere {
                    mutate(state().copy(loading = false,
                                        coordx = it.latitude,
                                        coordy = it.longitude))
                }
            }
            (action is LocateAddressFromCoords) -> {
                mutate(state().copy(loading = true))
                requestAddress(state().coordx, state().coordy) {
                    updateAddress(it)
                }
            }
            (action is LocateCoordsFromAddress) -> {
                mutate(state().copy(loading = true))
                requestCoordsFromAddress(state().address + ", " + state().city + ", " + state().state + ", " + state().zip)
                { coordx, coordy ->
                    mutate(state().copy(loading = false,
                                        coordx = coordx,
                                        coordy = coordy))
                }
            }
            (action is SelectRealtor) -> {
                mutate(state().copy(realtorId = action.realtorId,
                                    realtorUsername = action.realtorUsername))
            }
            (action is SubmitProperty) -> {
                submitProperty(action.property)
            }
            (action is LoadProperty) -> {
                loadProperty(action.property)
            }
            else -> { throw RuntimeException(action.toString()) }
        }
    }

    fun onPermissionGranted() {
        lastPermissionAction?.also { actions.onNext(it) }
    }

    private fun updateAddress(it: Address?) {
        if (it == null) {
            mutate(state().copy(loading = false, address = state().address))
        } else {
            var address = it.getAddressLine(0)
            if (address.contains(',')) address = address.slice(0..(address.indexOf(',')-1))
            mutate(state().copy(loading = false,
                address = address,
                city = it.getLocality(),
                state = getAbbrevForState(it.getAdminArea()),
                zip = it.getPostalCode()
            ))
        }
    }

    private fun requestCoordsHere(callback: (Location) -> Unit) {
        if ((fragment.activity as MainActivity).checkPermissions()) {
            try {
                LocationServices.getFusedLocationProviderClient(fragment.activity!!).lastLocation
                    .addOnSuccessListener(callback)
            } catch (e: SecurityException) { }
        }
    }

    private fun requestAddress(x: Double, y: Double, callback: (Address?) -> Unit) {
        ioThread {
            try {
                val addresses = geocoder.getFromLocation(x, y, 1)
                if (addresses.size > 0) callback(addresses[0])
                else callback(null)
            } catch (e: Exception) {
                callback(null)
            }
        }
    }

    private fun requestCoordsFromAddress(address: String, callback: (Double, Double) -> Unit) {
        ioThread {
            try {
                val addresses = geocoder.getFromLocationName(address, 1)
                if (addresses.size > 0) {
                    callback(addresses[0].latitude, addresses[0].longitude)
                } else {
                    callback(0.0, 0.0)
                }
            } catch (e: Exception) {
                callback(0.0, 0.0)
            }
        }
    }

    private fun submitProperty(property: Prop) {
        repo.putProp(property)
        fragment.makeToast("Property listing saved.")
        Rudder.navBack()
    }

    private fun loadProperty(prop: Prop) {
        mutate(state().copy(
            loading = false,
            loaded = true,
            coordx = prop.coordx,
            coordy = prop.coordy,
            name = prop.name,
            desc = prop.desc,
            sqft = prop.sqft,
            rent = prop.rent,
            rooms = prop.rooms,
            addtime = prop.addtime,
            available = prop.available,
            realtorId = prop.realtorId,
            address = prop.address,
            city = prop.city,
            state = prop.state,
            zip = prop.zip
        ))
    }

    private fun getAbbrevForState(state: String) =
            when (state) {
                "Alabama" -> "AL"
                "Alaska" -> "AK"
                "Arizona" -> "AZ"
                "Arkansas" -> "AR"
                "California" -> "CA"
                "Colorado" -> "CO"
                "Connecticut" -> "CT"
                "Delaware" -> "DE"
                "District of Columbia" -> "DC"
                "Florida" -> "FL"
                "Georgia" -> "GA"
                "Hawaii" -> "HI"
                "Idaho" -> "ID"
                "Illinois" -> "IL"
                "Indiana" -> "IN"
                "Iowa" -> "IA"
                "Kansas" -> "KS"
                "Kentucky" -> "KY"
                "Louisiana" -> "LA"
                "Maine" -> "ME"
                "Maryland" -> "MD"
                "Massachusetts" -> "MA"
                "Michigan" -> "MI"
                "Minnesota" -> "MN"
                "Mississippi" -> "MS"
                "Missouri" -> "MO"
                "Montana" -> "MT"
                "Nebraska" -> "NE"
                "Nevada" -> "NV"
                "New Hampshire" -> "NH"
                "New Jersey" -> "NJ"
                "New Mexico" -> "NM"
                "New York" -> "NY"
                "North Carolina" -> "NC"
                "North Dakota" -> "ND"
                "Ohio" -> "OH"
                "Oklahoma" -> "OK"
                "Oregon" -> "OR"
                "Pennsylvania" -> "PA"
                "Rhode Island" -> "RI"
                "South Carolina" -> "SC"
                "South Dakota" -> "SD"
                "Tennessee" -> "TN"
                "Texas" -> "TX"
                "Utah" -> "UT"
                "Vermont" -> "VT"
                "Virginia" -> "VA"
                "Washington" -> "WA"
                "West Virginia" -> "WV"
                "Wisconsin" -> "WI"
                "Wyoming" -> "WY"
                else -> state
            }
}