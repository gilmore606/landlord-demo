package com.dlfsystems.landlord.screens.propdetail

import com.dlfsystems.landlord.screens.base.Action


class LocateAddress(): Action

class LocateCoords(): Action

class LocateCoordsFromAddress(
    val address: String
): Action

class ReceiveCoords(
    val x: Long,
    val y: Long
): Action