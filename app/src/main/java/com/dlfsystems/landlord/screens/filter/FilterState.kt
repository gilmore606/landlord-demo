package com.dlfsystems.landlord.screens.filter

import com.dlfsystems.landlord.screens.base.BaseState

data class FilterState(
    val sizemin: Int = 0,
    val sizemax: Int = 0,
    val pricemin: Int = 0,
    val pricemax: Int = 0,
    val rooms: Int = 0
) : BaseState()