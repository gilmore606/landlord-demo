package com.dlfsystems.landlord.screens.propdetail

import com.dlfsystems.landlord.screens.base.BaseState

data class PropdetailState(
    val propId: String = "",
    val loading: Boolean = false,
    val coordx: Double = 0.0,
    val coordy: Double = 0.0,
    val address: String = ""
) : BaseState()