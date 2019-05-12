package com.dlfsystems.landlord.data.model

data class User(val id: Int,
                var name: String,
                var isRealtor: Boolean,
                var isAdmin: Boolean)