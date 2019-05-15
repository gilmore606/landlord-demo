package com.dlfsystems.landlord.data

import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.data.model.User
import com.google.firebase.database.*

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

    override fun deleteUser(userid: String) {
        repo.child("users").child(userid).setValue(null)
    }

    override fun getUsers(): Query {
        return repo.child("").child("users")
    }

    override fun getRealtors(callback: (List<User>) -> Unit) {
        repo.child("users").orderByChild("isRealtor").equalTo(true)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    callback(
                        dataSnapshot.children.mapNotNull {
                            it.getValue<User>(User::class.java)
                        }
                    )
                }
                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }

    fun getProps(): Query {
        return repo.child("").child("props")
    }

    override fun putProp(prop: Prop) {
        var propToPut = prop
        if (prop.id == "") {
            propToPut = prop.copy(id = repo.child("props").push().key ?: "")
        }
        repo.child("props").child(propToPut.id).setValue(propToPut)
    }
}