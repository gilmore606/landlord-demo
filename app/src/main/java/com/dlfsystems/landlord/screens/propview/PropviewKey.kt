package com.dlfsystems.landlord.screens.propview

import android.os.Bundle
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.nav.FragAnimPair
import com.dlfsystems.landlord.screens.base.BaseKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PropviewKey(val propId: String) : BaseKey() {
    override fun createFragment() = PropviewFragment().apply {
        arguments = (arguments ?: Bundle()).also {
            it.putSerializable("propId", propId)
        }
    }

    override fun getAnimation() =
        FragAnimPair(R.anim.grow_fade_in_from_bottom, R.anim.stationary)

    override fun getBackAnimation() =
        FragAnimPair(R.anim.stationary, R.anim.shrink_fade_out_from_bottom)
}