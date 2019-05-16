package com.dlfsystems.landlord.screens.propmap

import android.os.Bundle
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
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_propmap.*

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
            populateMarkers()
        }

        propmap_filterbutton.setOnClickListener {
            Rudder.navTo(FilterKey())
        }

        disposables += Rudder.filter.subscribe {
            actions.onNext(ChangeFilter(it))
        }
    }

    override fun render(state: BaseState) {
        populateMarkers()
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
        map?.let { stateHolder.mutate(state().copy(coord = it.cameraPosition.target)) }
        propmap_mapview.onPause()
        super.onPause()
    }
    override fun onResume() {
        super.onResume()
        propmap_mapview.onResume()
        moveCamera(state().coord)
    }
    override fun onStart() {
        super.onStart()
        propmap_mapview.onStart()
    }
    override fun onStop() {
        super.onStop()
        propmap_mapview.onStop()
    }

    private fun moveCamera(coord: LatLng) {

    }

    private fun populateMarkers() {
        repo.getFilteredProps(state().filter) { props ->
            map?.clear()
            props.forEach {
                val marker = MarkerOptions().position(LatLng(it.coordx, it.coordy))
                    .title(it.name)
                    .snippet(it.getSnippet())
                map?.addMarker(marker)
            }
        }
    }
}