package com.dlfsystems.landlord.screens.login

import com.dlfsystems.landlord.screens.base.BaseKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginKey(val tag: String) : BaseKey() {
    constructor(): this("LoginKey")
    override fun createFragment() = LoginFragment()
}