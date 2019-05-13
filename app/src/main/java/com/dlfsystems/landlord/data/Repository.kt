package com.dlfsystems.landlord.data

import com.dlfsystems.landlord.data.model.User
import com.google.firebase.database.Query

interface Repository {

    fun putUser(user: User)
    fun getUser(uid: String, callback: (User) -> Unit)
    fun deleteUser(userid: String)
    fun getUsers(): Query
}