package com.dlfsystems.landlord.screens.filter

import android.view.View
import com.dlfsystems.landlord.*
import com.dlfsystems.landlord.data.model.PropFilter
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.include_filterbar.*

class FilterFragment : BaseFragment() {

    val prefs by lazy { Prefs(context!!) }

    override val layoutResource = R.layout.fragment_filter
    override val presenter = FilterPresenter(this)
    override fun defaultState(): BaseState {
        val filter = prefs.searchFilter
        return FilterState(sizemin = filter.sizemin, sizemax = filter.sizemax,
                            pricemin = filter.pricemin, pricemax = filter.pricemax,
                            rooms = filter.rooms)
    }

    fun state() = stateHolder.state.value as FilterState

    override fun subscribeUI(view: View) {
        filterbar_edit_button.setImageDrawable(resources.getDrawable(R.drawable.ic_undo))
        filterbar_edit_button.setOnClickListener {
            actions.onNext(Cancel())
        }

        filter_apply_button.setOnClickListener {
            actions.onNext(ApplyFilter())
        }

        filter_clear_button.setOnClickListener {
            actions.onNext(ClearFilter())
        }

        filter_pricemin.validateNumeric({ true }, "Minimum rent", {
            stateHolder.mutate(state().copy(pricemin = it.toIntOrZero()))
        })
        filter_pricemax.validateNumeric({ isValidPricemax(it) }, "Maximum rent (0 for no limit)", {
            stateHolder.mutate(state().copy(pricemax = it.toIntOrZero()))
        })
        filter_sqftmin.validateNumeric({ true }, "Minimum floor space", {
            stateHolder.mutate(state().copy(sizemin = it.toIntOrZero()))
        })
        filter_sqftmax.validateNumeric({ isValidSizemax(it) }, "Maximum floor space (0 for no limit)", {
            stateHolder.mutate(state().copy(sizemax = it.toIntOrZero()))
        })
        filter_rooms.validateNumeric({ true }, "Minimum rooms", {
            stateHolder.mutate(state().copy(rooms = it.toIntOrZero()))
        })
    }

    override fun render(state: BaseState) {
        state as FilterState

        filter_pricemin.setIfChanged(state.pricemin.toString())
        filter_pricemax.setIfChanged(state.pricemax.toString())
        filter_rooms.setIfChanged(state.rooms.toString())
        filter_sqftmin.setIfChanged(state.sizemin.toString())
        filter_sqftmax.setIfChanged(state.sizemax.toString())

        filter_apply_button.isEnabled = isSubmittable()

        filterbar_text.text = filterFromState(state).description()
    }

    private fun isSubmittable(): Boolean {
        return ((filter_pricemin.error == null) and (filter_pricemax.error == null) and
                (filter_rooms.error == null) and (filter_sqftmin.error == null) and
                (filter_sqftmax.error == null))
    }

    private fun isValidPricemax(pricestr: String): Boolean {
        val max = pricestr.toIntOrNull() ?: 0
        val min = filter_pricemin.text.toString().toIntOrNull() ?: 0
        if ((min > 0) and (max > 0) and (max < min)) return false
        return true
    }

    private fun isValidSizemax(sizestr: String): Boolean {
        val max = sizestr.toIntOrNull() ?: 0
        val min = filter_sqftmin.text.toString().toIntOrNull() ?: 0
        if ((min > 0) and (max > 0) and (max < min)) return false
        return true
    }

    private fun filterFromState(state: FilterState): PropFilter =
        PropFilter(
            sizemin = state.sizemin,
            sizemax = state.sizemax,
            pricemin = state.pricemin,
            pricemax = state.pricemax,
            rooms = state.rooms
        )
}