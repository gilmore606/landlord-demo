package com.dlfsystems.landlord.nav

import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseKey
import com.dlfsystems.landlord.screens.login.LoginFragment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BackKey(val tag: String) : BaseKey() {
    constructor(): this("BackKey")
    override fun createFragment() = LoginFragment()
}