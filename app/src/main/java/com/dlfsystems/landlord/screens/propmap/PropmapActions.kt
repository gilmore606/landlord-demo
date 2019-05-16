package com.dlfsystems.landlord.screens.propmap

import com.dlfsystems.landlord.data.model.PropFilter
import com.dlfsystems.landlord.screens.base.Action

class LoadProperties() : Action

class ChangeFilter(
    val filter: PropFilter
) : Action

class ViewProperty(
    val propId: String
) : Action