package com.dlfsystems.landlord.screens.userdetail

import android.os.Bundle
import com.dlfsystems.landlord.screens.base.BaseKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserdetailKey(val userId: String) : BaseKey() {
    override fun createFragment() = UserdetailFragment().apply {
        arguments = (arguments ?: Bundle()).also {
            it.putSerializable("userId", userId)
        }
    }
}