package com.dlfsystems.landlord.screens.propmap

import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.data.model.PropFilter
import com.dlfsystems.landlord.screens.base.BaseState

data class PropmapState(
    val loading: Boolean = false,
    val coordx: Double = 37.814,
    val coordy: Double = -122.259,
    val zoom: Float = 12f,
    val filter: PropFilter,
    val props: List<Prop>? = null,
    val markerIds: List<String>? = null
) : BaseState()