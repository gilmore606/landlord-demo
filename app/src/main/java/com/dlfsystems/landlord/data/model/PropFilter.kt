package com.dlfsystems.landlord.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class PropFilter(var mineOnly: Boolean = false,
                      var includeRented: Boolean = true,
                      var sizemin: Int = 0,
                      var sizemax: Int = 0,
                      var pricemin: Int = 0,
                      var pricemax: Int = 0,
                      var rooms: Int = 0) : Parcelable, Serializable {

    fun matches(prop: Prop): Boolean {
        if (!prop.available and !includeRented) return false
        if (prop.sqft < sizemin) return false
        if ((sizemax > 0) and (prop.sqft > sizemax)) return false
        if (prop.rent < pricemin) return false
        if ((pricemax > 0) and (prop.rent > pricemax)) return false
        if (prop.rooms < rooms) return false
        return true
    }

    fun restrictForUser(user: User): PropFilter =
        copy(
            includeRented = user.isAdmin or user.isRealtor
        )

    fun description(): String {
        var d = ""

        if ((sizemin == 0) and (sizemax > 0))
            d += sizemax.toString() + " sq ft or less  "

        if ((sizemax == 0) and (sizemin > 0))
            d += sizemin.toString() + "+ sq ft  "

        if ((sizemin > 0) and (sizemax > 0))
            d += sizemin.toString() + " - " + sizemax.toString() + " sq ft  "

        if ((pricemin == 0) and (pricemax > 0))
            d += "$" + pricemax.toString() + " or less  "

        if ((pricemax == 0) and (pricemin > 0))
            d += "$" + pricemin.toString() + " and up  "

        if ((pricemin > 0) and (pricemax > 0))
            d += "$" + pricemin.toString() + " - $" + pricemax.toString() + "  "

        if ((rooms > 0))
            d += rooms.toString() + "+ rooms  "

        return if (d == "") "all properties" else d
    }
}