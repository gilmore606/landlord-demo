package com.dlfsystems.landlord.screens.propdetail

import android.os.Bundle
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.nav.FragAnimPair
import com.dlfsystems.landlord.screens.base.BaseKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PropdetailKey(val propId: String) : BaseKey() {
    override fun createFragment() = PropdetailFragment().apply {
        arguments = (arguments ?: Bundle()).also {
            it.putSerializable("propId", propId)
        }
    }

    override fun getAnimation(): FragAnimPair =
        FragAnimPair(R.anim.slide_in_right, R.anim.slide_out_right)

    override fun getBackAnimation(): FragAnimPair =
        FragAnimPair(R.anim.slide_in_left, R.anim.slide_out_left)
}