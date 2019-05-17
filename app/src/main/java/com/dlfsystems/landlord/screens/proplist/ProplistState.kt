package com.dlfsystems.landlord.screens.proplist

import com.dlfsystems.landlord.data.model.PropFilter
import com.dlfsystems.landlord.screens.base.BaseState

data class ProplistState(
    val filter: PropFilter
) : BaseState()