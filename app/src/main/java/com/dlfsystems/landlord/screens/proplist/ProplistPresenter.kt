package com.dlfsystems.landlord.screens.proplist

import com.dlfsystems.landlord.Prefs
import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.data.model.Prop
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
    val prefs by lazy { Prefs(fragment.context!!) }

    override fun hearAction(action: Action) {
        when {
            (action is InitialState) -> {
                mutate(state().copy(loading = true))
                loadProperties(state().filter)
            }
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
            (action is SortBy) -> {
                val state = state()
                mutate(state.copy(sortBy = action.position,
                                    props = sortProperties(state.props, action.position)))
            }
            else -> { throw RuntimeException(action.toString()) }
        }
    }

    private fun loadProperties(filter: PropFilter) {
        repo.getFilteredProps(filter.restrictForUser(prefs.user!!)) {
            val state = state()
            mutate(state.copy(loading = false, props = sortProperties(it, state.sortBy)))
        }
    }

    private fun sortProperties(props: List<Prop>, sortBy: Int): List<Prop> {
        return props.sortedBy {
            when (sortBy) {
                0 -> { it.rent }
                1 -> { -it.rent }
                2 -> { -it.sqft }
                3 -> { -it.rooms }
                else -> { throw RuntimeException(state().toString()) }
            }
        }
    }
}