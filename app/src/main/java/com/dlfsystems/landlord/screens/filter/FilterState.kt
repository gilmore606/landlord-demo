package com.dlfsystems.landlord.screens.filter

import com.dlfsystems.landlord.data.model.PropFilter
import com.dlfsystems.landlord.screens.base.BaseState

data class FilterState(
    val filter: PropFilter
) : BaseState()