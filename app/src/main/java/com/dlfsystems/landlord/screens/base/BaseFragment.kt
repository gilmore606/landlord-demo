package com.dlfsystems.landlord.screens.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProvider
import com.dlfsystems.landlord.MainActivity
import com.dlfsystems.landlord.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

abstract class BaseFragment : androidx.fragment.app.Fragment() {

    abstract val layoutResource: Int
    abstract val presenter: BasePresenter
    open val showToolbar = true

    lateinit var stateHolder: StateHolder
    var previousState: BaseState? = null
    val disposables = CompositeDisposable()
    val actions = PublishSubject.create<Action>()

    override fun onDestroy() {
        disposables.dispose()
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?) : View? {
        val view = inflater.inflate(layoutResource, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val initialState: BaseState =
            arguments?.let { makeStateFromArguments(it) } ?: defaultState()

        stateHolder = ViewModelProvider(this, SavedStateVMFactory(this))
            .get(StateHolder::class.java)

        disposables += stateHolder.state
            // .toFlowable(BackpressureStrategy.LATEST)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (view != null) doRender(it)
            }

        presenter.injectState(stateHolder, initialState, actions)
    }

    override fun onResume() {
        super.onResume()
        view?.also { subscribeUI(it) }
    }

    open fun subscribeUI(view: View) { }

    open fun makeStateFromArguments(arguments: Bundle) = defaultState()

    abstract fun defaultState(): BaseState

    @CallSuper
    open fun onShowFromBackStack() {
        doRender(stateHolder.state.value ?: defaultState())
    }

    private fun doRender(state: BaseState) {
        Timber.d("RENDER " + state.toString())
        (activity as MainActivity).toggleToolbar(showToolbar)
        render(state)
        previousState = state
    }

    open fun render(state: BaseState) { }

    open fun onPermissionGranted() { }

    fun makeToast(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun confirmAndDo(title: String, message: String, doIt: () -> Unit) {
        AlertDialog.Builder(context!!)
            .setTitle(title).setMessage(message)
            .setNegativeButton("Cancel") { _,_ -> }
            .setPositiveButton("Yes") { _,_ ->
                doIt()
            }
            .create().show()
    }
}