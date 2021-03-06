package com.dlfsystems.landlord.screens.propview

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import com.dlfsystems.landlord.Prefs
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState
import com.dlfsystems.landlord.setIfChanged
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_propview.*
import java.text.SimpleDateFormat
import java.util.*

class PropviewFragment : BaseFragment() {

    override val layoutResource = R.layout.fragment_propview
    override val presenter = PropviewPresenter(this)
    override fun defaultState() = PropviewState()

    fun state() = stateHolder.state.value as PropviewState

    val repo = FirebaseRepository()
    val prefs by lazy { Prefs(context!!) }
    val dateFormat = SimpleDateFormat("MM/dd/yyyy")

    var map: GoogleMap? = null

    override fun makeStateFromArguments(arguments: Bundle): BaseState =
            PropviewState(propId = arguments.getSerializable("propId") as String)

    override fun subscribeUI(view: View) {
        propview_mapview.getMapAsync {
            map = it
            moveCamera(state().coordx, state().coordy, state().name)
        }

        val isAdmin = prefs.user!!.isAdmin
        val isRealtor = prefs.user!!.isRealtor

        propview_edit_button.visibility = if (isAdmin or isRealtor) View.VISIBLE else View.GONE
        propview_delete_button.visibility = if (isAdmin or isRealtor) View.VISIBLE else View.GONE
        propview_available_button.visibility = if (isRealtor) View.VISIBLE else View.GONE

        propview_edit_button.setOnClickListener {
            actions.onNext(EditProperty(state().propId))
        }
        propview_delete_button.setOnClickListener {
            actions.onNext(DeleteProperty(state().propId))
        }
        propview_available_button.setOnClickListener {
            actions.onNext(SetAvailable(state().propId, !state().available))
        }
        propview_realtor.setOnClickListener {
            actions.onNext(EmailRealtor(state().realtorName, prefs.user!!.username, state().address))
        }
    }

    override fun render(state: BaseState) {
        state as PropviewState

        if (state.loading) {
            propview_loader.visibility = View.VISIBLE
            propview_content.visibility = View.GONE
        } else {
            propview_loader.visibility = View.GONE
            propview_content.visibility = View.VISIBLE

            moveCamera(state.coordx, state.coordy, state.name)

            propview_name.setIfChanged(state.name)
            propview_available.text =
                if (state.available) "" else "Unavailable"
            propview_available_button.text =
                if (state.available) "unavailable" else "available"
            propview_rent.setIfChanged("$" + state.rent.toString() + "/mo")
            propview_sqft.setIfChanged(state.sqft.toString() + " sq ft")
            propview_rooms.setIfChanged(state.rooms.toString() + " rooms")
            propview_realtor.setIfChanged(state.realtorName.replace("@", "@\n"))
            propview_realtor.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            propview_date.setIfChanged(dateFormat.format(Date(state.addtime)))
            propview_desc.setIfChanged("\"" + state.desc + "\"")
            propview_address.setIfChanged(state.address + "\n" + state.city + ", " + state.state + " " + state.zip)
        }
    }

    override fun onShowFromBackStack() {
        super.onShowFromBackStack()
        actions.onNext(Reload())
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
        super.onDestroyView()
        propview_mapview.onDestroy()
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