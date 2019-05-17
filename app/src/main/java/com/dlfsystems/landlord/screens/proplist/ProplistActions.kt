package com.dlfsystems.landlord.screens.proplist

import com.dlfsystems.landlord.data.model.PropFilter
import com.dlfsystems.landlord.screens.base.Action

class LoadProperties() : Action

class ViewProperty(
    val propId: String
) : Action

class ChangeFilter(
    val filter: PropFilter
) : Action