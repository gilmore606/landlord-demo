package com.dlfsystems.landlord.screens.filter

import com.dlfsystems.landlord.screens.base.BaseKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterKey(val tag: String = "FilterKey") : BaseKey() {
    override fun createFragment() = FilterFragment()
}