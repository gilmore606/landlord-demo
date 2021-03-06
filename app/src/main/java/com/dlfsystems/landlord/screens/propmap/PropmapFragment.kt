package com.dlfsystems.landlord.screens.propmap

import android.os.Bundle
import android.view.View
import com.dlfsystems.landlord.Prefs
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.plusAssign
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState
import com.dlfsystems.landlord.screens.filter.FilterKey
import com.dlfsystems.landlord.setIfChanged
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_propmap.*
import kotlinx.android.synthetic.main.include_filterbar.*

class PropmapFragment : BaseFragment() {

    val prefs by lazy { Prefs(context!!) }

    override val layoutResource = R.layout.fragment_propmap
    override val presenter = PropmapPresenter(this)
    override fun defaultState() = PropmapState(filter = prefs.searchFilter)

    fun state() = stateHolder.state.value as PropmapState

    val repo = FirebaseRepository()

    var map: GoogleMap? = null

    override fun subscribeUI(view: View) {
        propmap_mapview.getMapAsync {
            map = it
            actions.onNext(LoadProperties())
            moveCamera(state().coordx, state().coordy, state().zoom)
            map?.setOnInfoWindowClickListener {
                actions.onNext(ViewProperty(propIdForMarkerId(it.id)))
            }
        }

        filterbar.setOnClickListener {
            Rudder.navTo(FilterKey())
        }
        filterbar_edit_button.setOnClickListener {
            Rudder.navTo(FilterKey())
        }

        disposables += Rudder.filter.subscribe {
            actions.onNext(ChangeFilter(it))
        }
    }

    override fun render(state: BaseState) {
        state as PropmapState

        filterbar_text?.setIfChanged(state.filter.description())

        if (state.markerIds == null) {
            map?.clear()
            if ((state.props?.size ?: 0 > 0) and !state.loading) {
                stateHolder.mutate(state().copy(markerIds = placeMarkers(state.props!!)))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        propmap_mapview.onCreate(savedInstanceState)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        propmap_mapview.onSaveInstanceState(outState)
    }
    override fun onDestroyView() {
        propmap_mapview.onDestroy()
        super.onDestroyView()
    }
    override fun onPause() {
        map?.let { stateHolder.mutate(state().copy(coordx = it.cameraPosition.target.latitude,
                                                    coordy = it.cameraPosition.target.longitude,
                                                    zoom = it.cameraPosition.zoom)) }
        propmap_mapview.onPause()
        super.onPause()
    }
    override fun onResume() {
        super.onResume()
        propmap_mapview.onResume()
    }
    override fun onStart() {
        super.onStart()
        propmap_mapview.onStart()
    }
    override fun onStop() {
        propmap_mapview.onStop()
        super.onStop()
    }

    override fun onShowFromBackStack() {
        super.onShowFromBackStack()
        actions.onNext(LoadProperties())
    }

    private fun moveCamera(coordx: Double, coordy: Double, zoom: Float) {
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(coordx, coordy), zoom))
    }

    fun placeMarkers(props: List<Prop>): List<String> {
        val markerIds = ArrayList<String>(0)
        props.forEach {
            val markerOptions = MarkerOptions().position(LatLng(it.coordx, it.coordy))
                .title(it.name)
                .icon(BitmapDescriptorFactory.defaultMarker(
                    if (it.available) BitmapDescriptorFactory.HUE_AZURE
                    else BitmapDescriptorFactory.HUE_ROSE
                ))
                .snippet(it.makeSnippet())
            map?.also {
                val marker = it.addMarker(markerOptions)
                markerIds.add(marker.id)
            }
        }
        return markerIds
    }

    private fun propIdForMarkerId(markerId: String): String {
        val state = state()
        return state.props!![state.markerIds?.indexOf(markerId)!!].id
    }
}