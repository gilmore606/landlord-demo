package com.dlfsystems.landlord.screens.propmap

import android.os.Bundle
import android.service.autofill.Validators.and
import android.service.autofill.Validators.or
import android.view.View
import com.dlfsystems.landlord.Prefs
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.data.model.PropFilter
import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.plusAssign
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState
import com.dlfsystems.landlord.screens.filter.FilterKey
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_propmap.*
import java.lang.RuntimeException

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

        propmap_filterbutton.setOnClickListener {
            Rudder.navTo(FilterKey())
        }

        disposables += Rudder.filter.subscribe {
            actions.onNext(ChangeFilter(it))
        }
    }

    override fun render(state: BaseState) {
        state as PropmapState

        propmap_filtertext?.text = state.filter.description()

        if ((state.markerIds == null) and (state.props?.size ?: 0 > 0) and !state.loading) {
            stateHolder.mutate(state().copy(markerIds = placeMarkers(state.props!!)))
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
        super.onStop()
        propmap_mapview.onStop()
    }

    private fun moveCamera(coordx: Double, coordy: Double, zoom: Float) {
        map?.let { it.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(coordx, coordy), zoom))}
    }

    fun clearMarkers() { map?.clear() }

    fun addMarker(markerOptions: MarkerOptions): String? {
        map?.also {
            val marker = it.addMarker(markerOptions)
            return marker.id
        }
        return null
    }

    private fun placeMarkers(props: List<Prop>): List<String> {
        var markerIds = ArrayList<String>(0)
        props.forEach {
            val markerOptions = MarkerOptions().position(LatLng(it.coordx, it.coordy))
                .title(it.name)
                .snippet(it.getSnippet())
            map?.also {
                val marker = it.addMarker(markerOptions)
                markerIds.add(marker.id)
            }
        }
        return markerIds
    }

    private fun propIdForMarkerId(markerId: String): String {
        if (state().markerIds == null) throw RuntimeException("markerIds was null when queried!")
        val i = state().markerIds?.indexOf(markerId) ?: -1
        if (i > 0) {
            return state().props!![i].id
        }
        throw RuntimeException("markerId not found on query!")
    }
}