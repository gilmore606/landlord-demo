package com.dlfsystems.landlord.data

import com.dlfsystems.landlord.data.model.User

interface Repository {

    fun putUser(user: User)
    fun getUser(uid: String, callback: (User) -> Unit)
}