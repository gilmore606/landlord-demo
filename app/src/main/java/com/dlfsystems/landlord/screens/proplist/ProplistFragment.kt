package com.dlfsystems.landlord.screens.proplist

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlfsystems.landlord.Prefs
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.plusAssign
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState
import com.dlfsystems.landlord.screens.filter.FilterKey
import kotlinx.android.synthetic.main.fragment_proplist.*
import kotlinx.android.synthetic.main.include_filterbar.*

class ProplistFragment : BaseFragment() {

    val prefs by lazy { Prefs(context!!) }

    override val layoutResource = R.layout.fragment_proplist
    override val presenter = ProplistPresenter(this)
    override fun defaultState() = ProplistState(filter = prefs.searchFilter)

    fun state() = stateHolder.state.value as ProplistState

    val repo = FirebaseRepository()

    lateinit var recyclerAdapter: ProplistRecyclerAdapter

    override fun subscribeUI(view: View) {
        proplist_recyclerview.layoutManager = LinearLayoutManager(context)
        recyclerAdapter = ProplistRecyclerAdapter(state().props, actions)
        proplist_recyclerview.adapter = recyclerAdapter

        actions.onNext(LoadProperties())

        filterbar.setOnClickListener {
            Rudder.navTo(FilterKey())
        }
        filterbar_edit_button.setOnClickListener {
            Rudder.navTo(FilterKey())
        }

        disposables += Rudder.filter.subscribe {
            actions.onNext(ChangeFilter(it))
        }
    }

    override fun render(state: BaseState) {
        state as ProplistState

        filterbar_text?.text = state.filter.description()

        recyclerAdapter.updateProps(state.props)
    }
}