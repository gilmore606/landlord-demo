package com.dlfsystems.landlord.screens.filter

import com.dlfsystems.landlord.Prefs
import com.dlfsystems.landlord.data.model.PropFilter
import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter
import java.lang.RuntimeException

class FilterPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    val prefs by lazy { Prefs(fragment.context!!) }

    fun state() = stateHolder.state.value as FilterState

    override fun hearAction(action: Action) {
        when {
            (action is InitialState) -> { }
            (action is ApplyFilter) -> {
                updateFilter()
                Rudder.navBack()
            }
            (action is Cancel) -> {
                Rudder.navBack()
            }
            (action is ClearFilter) -> {
                mutate(state().copy(
                    sizemin = 0,
                    sizemax = 0,
                    pricemin = 0,
                    pricemax = 0,
                    rooms = 0
                ))
            }
            else -> { throw RuntimeException(action.toString()) }
        }
    }

    private fun updateFilter() {
        val filter = filterFromState(state())
        prefs.searchFilter = filter
        Rudder.updateFilter(filter)
    }

    private fun filterFromState(state: FilterState): PropFilter {
        var filter = PropFilter(
            rented = true,
            unrented = true,
            sizemin = state.sizemin,
            sizemax = state.sizemax,
            pricemin = state.pricemin,
            pricemax = state.pricemax,
            rooms = state.rooms
        )
        return filter
    }
}