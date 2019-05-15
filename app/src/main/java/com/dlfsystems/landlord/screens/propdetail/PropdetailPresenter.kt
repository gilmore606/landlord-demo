package com.dlfsystems.landlord.screens.propdetail

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import com.dlfsystems.landlord.MainActivity
import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import java.lang.RuntimeException
import java.util.*

class PropdetailPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    fun state() = stateHolder.state.value as PropdetailState

    val repo = FirebaseRepository()

    override fun hearAction(action: Action) {
        when {
            (action is LocateAddress) -> {

            }
            (action is LocateCoords) -> {
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
                    mutate(state().copy(loading = false,
                                        address = if (it == null) state().address
                                                  else it.getAddressLine(0)))
                }
            }
            (action is LocateCoordsFromAddress) -> {

            }
            else -> { throw RuntimeException(action.toString()) }
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
        try {
            val addresses = Geocoder(fragment.activity, Locale.getDefault()).getFromLocation(x, y, 1)
            if (addresses.size > 0) callback(addresses[0])
            else callback(null)
        } catch (e: Exception) { callback(null) }
    }
}