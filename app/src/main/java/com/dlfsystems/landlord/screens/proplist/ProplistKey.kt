package com.dlfsystems.landlord.screens.proplist

import com.dlfsystems.landlord.screens.base.BaseKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProplistKey(val tag: String) : BaseKey() {
    constructor(): this("ProplistKey")
    override fun createFragment() = ProplistFragment()
}