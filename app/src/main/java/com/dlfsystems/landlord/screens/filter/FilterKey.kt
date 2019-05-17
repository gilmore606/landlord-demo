package com.dlfsystems.landlord.screens.filter

import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.nav.FragAnimPair
import com.dlfsystems.landlord.screens.base.BaseKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterKey(val tag: String) : BaseKey() {
    constructor(): this("FilterKey")

    override fun createFragment() = FilterFragment()

}