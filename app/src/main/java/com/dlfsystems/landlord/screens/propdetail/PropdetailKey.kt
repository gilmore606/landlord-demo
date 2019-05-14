package com.dlfsystems.landlord.screens.propdetail

import android.os.Bundle
import com.dlfsystems.landlord.screens.base.BaseKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PropdetailKey(val propId: String) : BaseKey() {
    override fun createFragment() = PropdetailFragment().apply {
        arguments = (arguments ?: Bundle()).also {
            it.putSerializable("propId", propId)
        }
    }
}