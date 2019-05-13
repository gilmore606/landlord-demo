package com.dlfsystems.landlord.data.model

data class User(val uid: String = "",
                var username: String = "???",
                var isRealtor: Boolean = false,
                var isAdmin: Boolean = false)