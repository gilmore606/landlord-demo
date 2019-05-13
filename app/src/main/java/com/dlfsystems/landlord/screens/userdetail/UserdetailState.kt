package com.dlfsystems.landlord.screens.userdetail

import com.dlfsystems.landlord.screens.base.BaseState

data class UserdetailState(
    val userId: String = "",
    val loading: Boolean = false,
    val username: String = "",
    val isRealtor: Boolean = false,
    val isAdmin: Boolean = false
) : BaseState()