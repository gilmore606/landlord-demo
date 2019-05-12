package com.dlfsystems.landlord.screens.proplist

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState
import kotlinx.android.synthetic.main.fragment_proplist.*

class ProplistFragment : BaseFragment() {

    override val layoutResource = R.layout.fragment_proplist
    override val presenter = ProplistPresenter(this)
    override fun defaultState() = ProplistState()

    fun state() = stateHolder.state.value as ProplistState

    val recyclerAdapter = ProplistRecyclerAdapter()

    override fun subscribeUI(view: View) {
        proplist_recyclerview.layoutManager = LinearLayoutManager(context)
        proplist_recyclerview.adapter = recyclerAdapter
    }

    override fun render(state: BaseState) {

    }
}