package com.dlfsystems.landlord.screens.base

import com.dlfsystems.landlord.plusAssign
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

abstract class BasePresenter(val fragment: BaseFragment) {

    lateinit var stateHolder: StateHolder

    val disposables = CompositeDisposable()

    fun mutate(newState: BaseState) = stateHolder.mutate(newState)

    fun connectStateHolder(stateHolder: StateHolder, initialState: BaseState) {
        this.stateHolder = stateHolder
        stateHolder.provideInitialState(initialState)
    }

    fun connectActions(actions: PublishSubject<Action>) {
        disposables += actions.subscribe { hearAction(it) }
    }

    open fun hearAction(action: Action) { }

    fun onDestroy() {
        disposables.dispose()
    }
}