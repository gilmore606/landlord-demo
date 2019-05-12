package com.dlfsystems.landlord.data.model

data class PropFilter(var mineOnly: Boolean = false,
    var rented: Boolean = true,
    var unrented: Boolean = true,
    var sizemin: Int = 0,
    var sizemax: Int = 20000,
    var pricemin: Int = 0,
    var pricemax: Int = 100000,
    var rooms: Int = 0)