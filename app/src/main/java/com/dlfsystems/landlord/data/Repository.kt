package com.dlfsystems.landlord.data

import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.data.model.User
import com.google.firebase.database.Query

interface Repository {

    fun putUser(user: User)
    fun getUser(uid: String, callback: (User) -> Unit)
    fun deleteUser(userid: String)
    fun getUsers(): Query
    fun getRealtors(callback: (List<User>) -> Unit)
    fun getProp(propId: String, callback: (Prop) -> Unit)
    fun putProp(prop: Prop)
    fun deleteProp(propId: String)
}