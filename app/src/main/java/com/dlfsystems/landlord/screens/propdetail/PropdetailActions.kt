package com.dlfsystems.landlord.screens.propdetail

import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.screens.base.Action


class LocateAddress(): Action

class LocateCoords(): Action

class LocateCoordsFromAddress(
    val address: String
): Action

class LocateAddressFromCoords(
    val x: Double,
    val y: Double
): Action

class SelectRealtor(
    val realtorId: String,
    val realtorUsername: String
): Action

class SubmitProperty(
    val property: Prop
): Action

class LoadProperty(
    val property: Prop
): Action
