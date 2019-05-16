package com.dlfsystems.landlord.screens.filter

import android.os.Bundle
import android.view.View
import com.dlfsystems.landlord.Prefs
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.data.model.PropFilter
import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState

class FilterFragment : BaseFragment() {

    val prefs by lazy { Prefs(context!!) }

    override val layoutResource = R.layout.fragment_filter
    override val presenter = FilterPresenter(this)
    override fun defaultState() = FilterState(prefs.searchFilter)

    fun state() = stateHolder.state.value as FilterState

    override fun subscribeUI(view: View) {

    }

    override fun render(state: BaseState) {

    }

    private fun updateFilter() {
        prefs.searchFilter = state().filter
        Rudder.updateFilter(state().filter)
    }
}