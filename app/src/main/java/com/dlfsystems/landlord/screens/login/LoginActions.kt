package com.dlfsystems.landlord.screens.login

import com.dlfsystems.landlord.screens.base.Action

class LogIn(
    val username: String,
    val password: String
) : Action

class Register(
    val username: String,
    val password: String
) : Action