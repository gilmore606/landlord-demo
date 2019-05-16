package com.dlfsystems.landlord.data.model

data class PropFilter(var mineOnly: Boolean = false,
    var rented: Boolean = true,
    var unrented: Boolean = true,
    var sizemin: Int = 0,
    var sizemax: Int = 20000,
    var pricemin: Int = 0,
    var pricemax: Int = 100000,
    var rooms: Int = 0) {

    fun matches(prop: Prop): Boolean {
        if (prop.available and !unrented) return false
        if (!prop.available and !rented) return false
        if (prop.sqft < sizemin) return false
        if (prop.sqft > sizemax) return false
        if (prop.rent < pricemin) return false
        if (prop.rent > pricemax) return false
        if (prop.rooms < rooms) return false
        return true
    }
}