package com.dlfsystems.landlord.screens.propdetail

import android.Manifest
import android.content.pm.PackageManager
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
}