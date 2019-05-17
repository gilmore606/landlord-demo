package com.dlfsystems.landlord.screens.proplist

import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.data.model.PropFilter
import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter
import com.dlfsystems.landlord.screens.propview.PropviewKey
import java.lang.RuntimeException

class ProplistPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    fun state() = stateHolder.state.value as ProplistState

    val repo = FirebaseRepository()

    override fun hearAction(action: Action) {
        when {
            (action is LoadProperties) -> {
                mutate(state().copy(loading = true))
                loadProperties(state().filter)
            }
            (action is ViewProperty) -> {
                Rudder.navTo(PropviewKey(action.propId))
            }
            (action is ChangeFilter) -> {
                mutate(state().copy(filter = action.filter))
                loadProperties(action.filter)
            }
            else -> { throw RuntimeException(action.toString()) }
        }
    }

    private fun loadProperties(filter: PropFilter) {
        repo.getFilteredProps(filter) {
            mutate(state().copy(loading = false, props = it))
        }
    }
}