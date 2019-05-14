package com.dlfsystems.landlord.screens.propmap

import com.dlfsystems.landlord.screens.base.BaseKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PropmapKey(val tag: String) : BaseKey() {
    constructor(): this("PropmapKey")
    override fun createFragment() = PropmapFragment()
}