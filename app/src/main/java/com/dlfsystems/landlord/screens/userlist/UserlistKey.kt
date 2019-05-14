package com.dlfsystems.landlord.screens.userlist

import com.dlfsystems.landlord.screens.base.BaseKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserlistKey(val tag: String) : BaseKey() {
    override val allowBack = false

    constructor(): this("UserlistKey")
    override fun createFragment() = UserlistFragment()
}