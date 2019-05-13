package com.dlfsystems.landlord.screens.userlist

import com.dlfsystems.landlord.screens.base.BaseKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserlistKey(val tag: String) : BaseKey() {
    constructor(): this("UserlistKey")
    override fun createFragment() = UserlistFragment()
}