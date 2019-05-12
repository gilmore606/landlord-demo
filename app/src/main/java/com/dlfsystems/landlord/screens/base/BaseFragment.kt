package com.dlfsystems.landlord.screens.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProvider
import com.dlfsystems.landlord.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

abstract class BaseFragment : androidx.fragment.app.Fragment() {

    abstract val layoutResource: Int
    abstract val presenter: BasePresenter
    lateinit var stateHolder: StateHolder
    var previousState: BaseState? = null
    val disposables = CompositeDisposable()
    val actions = PublishSubject.create<Action>()

    val requireArguments
        get() = this.arguments ?: throw IllegalStateException("Fragment arguments should exist!")

    fun <T : BaseKey> getKey(): T? = requireArguments.getParcelable<T>("KEY")

    override fun onDestroy() {
        disposables.dispose()
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?) : View? {
        val view = inflater.inflate(layoutResource, container, false)
        onCreateView(view)

        val initialState: BaseState =
            arguments?.let { makeStateFromArguments(it) } ?: defaultState()

        stateHolder = ViewModelProvider(this, SavedStateVMFactory(this))
            .get(StateHolder::class.java)

        presenter.connectStateHolder(stateHolder, initialState)
        presenter.connectActions(actions)

        disposables += stateHolder.state.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                render(it)
                previousState = it
            }

        subscribeUI(view)

        return view
    }

    open fun subscribeUI(view: View) { }

    open fun makeStateFromArguments(arguments: Bundle) = defaultState()

    abstract fun defaultState(): BaseState

    open fun onCreateView(view: View) { }

    open fun render(state: BaseState) { }

}