package com.dlfsystems.landlord.screens.proplist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dlfsystems.landlord.R

class ProplistRecyclerHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bindProp() {

    }
}

class ProplistRecyclerAdapter : RecyclerView.Adapter<ProplistRecyclerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProplistRecyclerHolder =
            ProplistRecyclerHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_proplist, parent, false))

    override fun getItemCount(): Int = 0

    override fun onBindViewHolder(holder: ProplistRecyclerHolder, position: Int) {

    }
}