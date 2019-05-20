package com.dlfsystems.landlord.data

import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.data.model.PropFilter
import com.dlfsystems.landlord.data.model.User
import com.google.firebase.database.*

class FirebaseRepository : Repository {

    val repo = FirebaseDatabase.getInstance().reference

    override fun putUser(user: User) {
        repo.child("users").child(user.uid).setValue(user)
    }

    override fun getUser(uid: String, callback: (User?) -> Unit) {
        repo.child("users").child(uid)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(e: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    callback(snapshot.getValue<User>(User::class.java))
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
        repo.child("users").orderByChild("realtor").equalTo(true)
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

    override fun getProp(propId: String, callback: (Prop) -> Unit) {
        repo.child("props").child(propId).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(e: DatabaseError) {

            }
            override fun onDataChange(snapshot: DataSnapshot) {
                callback(snapshot.getValue<Prop>(Prop::class.java)!!)
            }
        })
    }

    override fun getFilteredProps(filter: PropFilter, callback: (List<Prop>) -> Unit) {
        repo.child("props").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val matches = ArrayList<Prop>(0)
                snapshot.children.forEach {
                    val prop = it.getValue<Prop>(Prop::class.java)
                    prop?.also { if (filter.matches(prop)) matches.add(prop) }
                }
                callback(matches)
            }
        })
    }

    override fun putProp(prop: Prop) {
        var propToPut = prop
        if (prop.id == "") {
            propToPut = prop.copy(id = repo.child("props").push().key ?: "")
        }
        repo.child("props").child(propToPut.id).setValue(propToPut)
    }

    override fun deleteProp(propId: String) {
        repo.child("props").child(propId).setValue(null)
    }
}