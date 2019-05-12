package com.dlfsystems.landlord.screens.login

import com.dlfsystems.landlord.screens.base.BaseState

data class LoginState(
    val username: String = "",
    val password: String = "",
    val waiting: Boolean = false,
    val lastError: String = ""
) : BaseState()