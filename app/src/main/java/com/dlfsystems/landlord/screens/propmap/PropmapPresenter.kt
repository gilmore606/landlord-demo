package com.dlfsystems.landlord.screens.propmap

import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.data.model.PropFilter
import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class PropmapPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    fun state() = stateHolder.state.value as PropmapState

    val repo = FirebaseRepository()

    override fun hearAction(action: Action) {
        when {
            (action is LoadProperties) -> {
                mutate(state().copy(loading = true))
                loadProperties(state().filter)
            }
            (action is ChangeFilter) -> {
                mutate(state().copy(filter = action.filter, loading = true))
                loadProperties(action.filter)
            }
            else -> { throw RuntimeException(action.toString()) }
        }
    }

    private fun loadProperties(filter: PropFilter) {
        repo.getFilteredProps(filter) {
            mutate(state().copy(loading = false, props = it, markerIds = null))
        }
    }
}