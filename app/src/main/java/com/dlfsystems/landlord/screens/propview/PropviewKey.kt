package com.dlfsystems.landlord.screens.propview

import android.os.Bundle
import com.dlfsystems.landlord.screens.base.BaseKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PropviewKey(val propId: String) : BaseKey() {
    override fun createFragment() = PropviewFragment().apply {
        arguments = (arguments ?: Bundle()).also {
            it.putSerializable("propId", propId)
        }
    }
}