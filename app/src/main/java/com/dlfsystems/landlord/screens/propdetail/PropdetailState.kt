package com.dlfsystems.landlord.screens.propdetail

import com.dlfsystems.landlord.screens.base.BaseState

data class PropdetailState(
    val propId: String = "",
    val loading: Boolean = false,
    val loaded: Boolean = false,
    val isNew: Boolean = true,
    val coordx: Double = 0.0,
    val coordy: Double = 0.0,
    val name: String = "",
    val desc: String = "",
    val sqft: Int = 0,
    val rent: Int = 0,
    val rooms: Int = 0,
    val addtime: Long = 0,
    val available: Boolean = true,
    val realtorId: String = "",
    val realtorUsername: String = "",
    val realtorList: List<String>? = null,
    val realtorIds: List<String>? = null,
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val zip: String = ""

) : BaseState()