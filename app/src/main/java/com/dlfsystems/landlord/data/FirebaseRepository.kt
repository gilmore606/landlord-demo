package com.dlfsystems.landlord.data

import com.dlfsystems.landlord.data.model.User
import com.google.firebase.database.FirebaseDatabase

class FirebaseRepository : Repository {

    val repo = FirebaseDatabase.getInstance().reference

    override fun putUser(user: User) {
        var userWithId = user
        if (user.uid == "") {
            val key = repo.child("users").push().key
            userWithId = user.copy(uid = key!!)
        }
        repo.child("users").child(userWithId.uid).setValue(userWithId)
    }

    override fun getUser(uid: String): User {

    }
}