package com.dlfsystems.landlord.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class PropFilter(var mineOnly: Boolean = false,
    var rented: Boolean = true,
    var unrented: Boolean = true,
    var sizemin: Int = 0,
    var sizemax: Int = 0,
    var pricemin: Int = 0,
    var pricemax: Int = 0,
    var rooms: Int = 0) : Parcelable, Serializable {

    fun matches(prop: Prop): Boolean {
        if (prop.available and !unrented) return false
        if (!prop.available and !rented) return false
        if (prop.sqft < sizemin) return false
        if ((sizemax > 0) and (prop.sqft > sizemax)) return false
        if (prop.rent < pricemin) return false
        if ((pricemax > 0) and (prop.rent > pricemax)) return false
        if (prop.rooms < rooms) return false
        return true
    }

    fun description(): String {
        var d = ""

        if ((sizemin == 0) and (sizemax > 0))
            d += "<" + sizemax.toString() + "sqft "

        if ((sizemax == 0) and (sizemin > 0))
            d += ">" + sizemin.toString() + "sqft "

        if ((sizemin > 0) and (sizemax > 0))
            d += sizemin.toString() + "-" + sizemax.toString() + "sqft "

        if ((pricemin == 0) and (pricemax > 0))
            d += "<$" + pricemax.toString() + " "

        if ((pricemax == 0) and (pricemin > 0))
            d += ">$" + pricemin.toString() + " "

        if ((pricemin > 0) and (pricemax > 0))
            d += "$" + pricemin.toString() + "-$" + pricemax.toString() + " "

        if ((rooms > 0))
            d += rooms.toString() + "+rooms "

        if (d == "") return "all properties"
        else return d
    }
}