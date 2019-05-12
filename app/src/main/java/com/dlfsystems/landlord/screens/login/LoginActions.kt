package com.dlfsystems.landlord.screens.login

import com.dlfsystems.landlord.screens.base.Action

class LoginAction(
    val username: String,
    val password: String
) : Action

class RegisterAction(
    val username: String,
    val password: String
) : Action