package com.dlfsystems.landlord.screens.propmap

import com.dlfsystems.landlord.screens.base.BaseState
import com.google.android.gms.maps.model.LatLng

data class PropmapState(
    val coord: LatLng = LatLng(0.0, 0.0)
) : BaseState()