package com.dlfsystems.landlord.screens.propdetail

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

class ReceiveCoords(
    val x: Double,
    val y: Double
): Action