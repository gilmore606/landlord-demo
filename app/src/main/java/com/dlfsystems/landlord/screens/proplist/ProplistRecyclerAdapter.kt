package com.dlfsystems.landlord.screens.proplist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.screens.base.Action
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_proplist.view.*

class ProplistHolder(val view: View, val prop: Prop? = null): RecyclerView.ViewHolder(view) {

    fun bind(prop: Prop, actions: PublishSubject<Action>) {
        view.setOnClickListener {
            actions.onNext(ViewProperty(prop.id))
        }
        view.setBackgroundColor(view.context.getColor(
            if (prop.available) R.color.white else R.color.lightred
        ))
        view.proprow_name.text = prop.name
        view.proprow_name.setTextColor(view.context.getColor(
            if (prop.available) R.color.black else R.color.darkred
        ))
        view.proprow_address.text = prop.address
        view.proprow_rent.text = "$" + prop.rent.toString()
        view.proprow_rooms.text = prop.rooms.toString() + " rm"
        view.proprow_sqft.text = prop.sqft.toString() + " sqft"
    }
}

class ProplistRecyclerAdapter(var props: List<Prop>, val actions: PublishSubject<Action>) :
        RecyclerView.Adapter<ProplistHolder>() {

    override fun getItemCount(): Int =
        props.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProplistHolder =
            ProplistHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_proplist, parent, false))

    override fun onBindViewHolder(holder: ProplistHolder, position: Int) =
            holder.bind(props[position], actions)

    fun updateProps(newprops: List<Prop>) {
        if (newprops != props) {
            props = newprops
            notifyDataSetChanged()
        }
    }
}