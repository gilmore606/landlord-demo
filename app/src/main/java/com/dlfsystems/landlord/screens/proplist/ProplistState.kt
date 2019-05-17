package com.dlfsystems.landlord.screens.proplist

import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.data.model.PropFilter
import com.dlfsystems.landlord.screens.base.BaseState

data class ProplistState(
    val loading: Boolean = false,
    val filter: PropFilter = PropFilter(),
    val props: List<Prop> = ArrayList<Prop>(0)
) : BaseState()