package com.dlfsystems.landlord.screens.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import io.reactivex.subjects.ReplaySubject

class StateHolder(private val savedState: SavedStateHandle) : ViewModel() {

    private var hasState = false

    val state = ReplaySubject.createWithSize<BaseState>(1)

    init {
        if (savedState.contains("state"))
            provideInitialState(savedState.get<BaseState>("state")!!)
    }

    fun mutate(newState: BaseState) {
        hasState = true
        state.onNext(newState)
        savedState.set("state", state.value)
    }

    fun provideInitialState(initialState: BaseState) {
        if (!hasState) mutate(initialState)
    }
}