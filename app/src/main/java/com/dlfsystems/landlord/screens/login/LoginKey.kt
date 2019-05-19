package com.dlfsystems.landlord.screens.login

import com.dlfsystems.landlord.screens.base.BaseKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginKey(val tag: String = "LoginKey") : BaseKey() {
    @IgnoredOnParcel
    override val allowBack = false

    override fun createFragment() = LoginFragment()
}