package com.dlfsystems.landlord.screens.proplist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.screens.base.Action
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_proplist.view.*

class ProplistHolder(val view: View, val prop: Prop? = null): RecyclerView.ViewHolder(view) {

    fun bind(prop: Prop, actions: PublishSubject<Action>) {
        view.setOnClickListener {
            actions.onNext(ViewProp(prop.id))
        }
        view.proprow_name.text = prop.name
        view.proprow_address.text = prop.address
    }
}

class ProplistRecyclerAdapter(options: FirebaseRecyclerOptions<Prop>, val actions: PublishSubject<Action>) :
        FirebaseRecyclerAdapter<Prop, ProplistHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProplistHolder =
            ProplistHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_proplist, parent, false))

    override fun onBindViewHolder(holder: ProplistHolder, position: Int, model: Prop) =
            holder.bind(model, actions)

    override fun onDataChanged() {

    }
}