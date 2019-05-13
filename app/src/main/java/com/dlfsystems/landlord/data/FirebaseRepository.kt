package com.dlfsystems.landlord.data

import com.dlfsystems.landlord.data.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseRepository : Repository {

    val repo = FirebaseDatabase.getInstance().reference

    override fun putUser(user: User) {
        repo.child("users").child(user.uid).setValue(user)
    }

    override fun getUser(uid: String, callback: (User) -> Unit) {
        repo.child("users").child(uid).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(e: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                callback(snapshot.getValue<User>(User::class.java)!!)
            }
        })
    }
}