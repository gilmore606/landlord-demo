package com.dlfsystems.landlord.data.model

import java.io.Serializable

data class Prop(val id: String = "",
                val coordx: Double = 0.0,
                val coordy: Double = 0.0,
                val name: String = "",
                val desc: String = "",
                val sqft: Int = 0,
                val rent: Int = 0,
                val rooms: Int = 0,
                val addtime: Long = 0,
                val available: Boolean = true,
                val realtorId: String = "",
                val address: String = "",
                val city: String = "",
                val state: String = "",
                val zip: String = ""
): Serializable {
    fun makeSnippet(): String {
        return rooms.toString() + " room " + sqft.toString() + "sqft $" + rent.toString() +"/mo"
    }
}
