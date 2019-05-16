package com.dlfsystems.landlord.screens.propmap

import com.dlfsystems.landlord.data.model.PropFilter
import com.dlfsystems.landlord.screens.base.BaseState
import com.google.android.gms.maps.model.LatLng

data class PropmapState(
    val coordx: Double = 37.814,
    val coordy: Double = -122.259,
    val zoom: Float = 12f,
    val filter: PropFilter
) : BaseState()