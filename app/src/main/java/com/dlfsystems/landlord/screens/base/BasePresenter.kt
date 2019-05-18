package com.dlfsystems.landlord.screens.base

import com.dlfsystems.landlord.plusAssign
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

abstract class BasePresenter(val fragment: BaseFragment) {

    class InitialState : Action

    lateinit var stateHolder: StateHolder
    lateinit var actions: PublishSubject<Action>

    val disposables = CompositeDisposable()

    fun mutate(newState: BaseState) = stateHolder.mutate(newState)

    fun injectInitialState(stateHolder: StateHolder,
                           initialState: BaseState,
                           actions: PublishSubject<Action>) {

        this.stateHolder = stateHolder
        this.actions = actions
        disposables += actions.subscribe { hearAction(it) }
        stateHolder.provideInitialState(initialState)
        actions.onNext(InitialState())
    }

    open fun hearAction(action: Action) { }

    fun onDestroy() {
        disposables.dispose()
    }
}