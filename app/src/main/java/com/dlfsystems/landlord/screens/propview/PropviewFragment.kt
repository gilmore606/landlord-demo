package com.dlfsystems.landlord.screens.propview

import android.os.Bundle
import android.service.autofill.Validators.or
import android.view.View
import com.dlfsystems.landlord.Prefs
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_propview.*

class PropviewFragment : BaseFragment() {

    override val layoutResource = R.layout.fragment_propview
    override val presenter = PropviewPresenter(this)
    override fun defaultState() = PropviewState()

    fun state() = stateHolder.state.value as PropviewState

    val repo = FirebaseRepository()
    val prefs by lazy { Prefs(context!!) }

    var map: GoogleMap? = null

    override fun makeStateFromArguments(arguments: Bundle): BaseState =
            PropviewState(
                propId = arguments.getSerializable("propId") as String,
                loading = true
            )

    override fun subscribeUI(view: View) {
        propview_mapview.getMapAsync {
            map = it
            moveCamera(state().coordx, state().coordy, state().name)
        }

        if ((prefs.user?.isAdmin ?: false) or (prefs.user?.isRealtor ?: false)) {
            propview_edit_button.visibility = View.VISIBLE
            propview_delete_button.visibility = View.VISIBLE
            propview_available_button.visibility = View.VISIBLE
        } else {
            propview_edit_button.visibility = View.GONE
            propview_delete_button.visibility = View.GONE
            propview_available_button.visibility = View.GONE
        }

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

            moveCamera(state.coordx, state.coordy, state.name)

            propview_available.text =
                if (state.available) "This property is available for rent."
                else "This property has been rented, and is no longer available."
            propview_available_button.text =
                if (state.available) "Set unavailable"
                else "Set available"

        } else {
            propview_loader.visibility = View.VISIBLE
            propview_content.visibility = View.GONE
        }
    }

    private fun moveCamera(coordx: Double, coordy: Double, title: String) {
        map?.let {
            it.clear()
            val markerOptions = MarkerOptions().position(LatLng(coordx, coordy))
                .title(title)
            it.addMarker(markerOptions)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(coordx, coordy), 14f))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        propview_mapview.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        propview_mapview.onSaveInstanceState(outState)
    }
    override fun onDestroyView() {
        propview_mapview.onDestroy()
        super.onDestroyView()
    }
    override fun onPause() {
        propview_mapview.onPause()
        super.onPause()
    }
    override fun onResume() {
        propview_mapview.onResume()
        super.onResume()
    }
    override fun onStart() {
        super.onStart()
        propview_mapview.onStart()
    }
    override fun onStop() {
        propview_mapview.onStop()
        super.onStop()
    }
}