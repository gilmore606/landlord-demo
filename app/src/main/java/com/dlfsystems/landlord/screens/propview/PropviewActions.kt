package com.dlfsystems.landlord.screens.propview

import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.screens.base.Action

class LoadProperty(
    val property: Prop
) : Action

class EditProperty(
    val propId: String
) : Action

class DeleteProperty(
    val propId: String
) : Action

class SetAvailable(
    val propId: String,
    val available: Boolean
) : Action

class LoadRealtorName(
    val name: String
) : Action