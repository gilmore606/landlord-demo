package com.dlfsystems.landlord.screens.userlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.data.model.User
import com.dlfsystems.landlord.screens.base.Action
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_userlist.view.*

class UserlistHolder(val view: View, val user: User? = null) : RecyclerView.ViewHolder(view) {

    fun bind(user: User, actions: PublishSubject<Action>) {
        view.setOnClickListener {
            actions.onNext(
                ViewUserAction(user.uid)
            )
        }
        view.userrow_name.text = user.username
        view.userrow_role.text = if (user.isAdmin and user.isRealtor) "Admin / Realtor"
            else if (user.isAdmin) "Admin"
            else if (user.isRealtor) "Realtor"
            else "Client"
    }
}

class UserlistRecyclerAdapter(options: FirebaseRecyclerOptions<User>, val actions: PublishSubject<Action>) :
    FirebaseRecyclerAdapter<User, UserlistHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserlistHolder =
        UserlistHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.item_userlist, parent, false))

    override fun onBindViewHolder(holder: UserlistHolder, position: Int, model: User) =
        holder.bind(model, actions)

    override fun onDataChanged() {

    }
}