package com.dlfsystems.landlord.screens.propmap

import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.nav.FragAnimPair
import com.dlfsystems.landlord.screens.base.BaseKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PropmapKey(val tag: String = "PropmapKey") : BaseKey() {
    override fun createFragment() = PropmapFragment()

    @IgnoredOnParcel
    override val allowBack = false

    override fun getAnimation(): FragAnimPair =
        FragAnimPair(R.anim.slide_in_right, R.anim.slide_out_right)

    override fun getBackAnimation(): FragAnimPair =
        FragAnimPair(R.anim.slide_in_left, R.anim.slide_out_left)
}