package com.dlfsystems.landlord.screens.propview

import android.os.Bundle
import android.view.View
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState
import kotlinx.android.synthetic.main.fragment_propview.*

class PropviewFragment : BaseFragment() {

    override val layoutResource = R.layout.fragment_propview
    override val presenter = PropviewPresenter(this)
    override fun defaultState() = PropviewState()

    fun state() = stateHolder.state.value as PropviewState

    val repo = FirebaseRepository()

    override fun makeStateFromArguments(arguments: Bundle): BaseState =
            PropviewState(
                propId = arguments.getSerializable("propId") as String,
                loading = true
            )

    override fun subscribeUI(view: View) {
        if (state().loading) {
            repo.getProp(state().propId) {
                actions.onNext(LoadProperty(it))
            }
        }

        propview_edit_button.setOnClickListener {
            actions.onNext(EditProperty(state().propId))
        }
        propview_delete_button.setOnClickListener {
            actions.onNext(DeleteProperty(state().propId))
        }
        propview_available_button.setOnClickListener {
            actions.onNext(SetAvailable(state().propId, !state().available))
        }
    }

    override fun render(state: BaseState) {
        state as PropviewState

        if (!state.loading) {
            propview_loader.visibility = View.GONE
            propview_content.visibility = View.VISIBLE
        } else {
            propview_loader.visibility = View.VISIBLE
            propview_content.visibility = View.GONE
        }
    }
}