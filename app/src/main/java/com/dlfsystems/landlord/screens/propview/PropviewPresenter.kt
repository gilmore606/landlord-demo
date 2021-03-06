package com.dlfsystems.landlord.screens.propview

import android.content.Intent
import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.data.model.Prop
import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter
import com.dlfsystems.landlord.screens.propdetail.PropdetailKey
import java.lang.RuntimeException

class PropviewPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    fun state() = stateHolder.state.value as PropviewState

    val repo = FirebaseRepository()

    override fun hearAction(action: Action) {
        when {
            (action is InitialState) -> {
                val state = state()
                if (!state.loaded) {
                    mutate(state.copy(loading = true))
                    repo.getProp(state.propId) {
                        actions.onNext(LoadProperty(it))
                    }
                }
            }
            (action is Reload) -> {
                mutate(state().copy(loaded = false))
                actions.onNext(InitialState())
            }
            (action is LoadProperty) -> {
                loadProperty(action.property)
            }
            (action is EditProperty) -> {
                Rudder.navTo(PropdetailKey(action.propId))
            }
            (action is DeleteProperty) -> {
                confirmAndDeleteProperty(action.propId)
            }
            (action is SetAvailable) -> {
                mutate(state().copy(available = action.available))
                updateProperty(propFromState())
            }
            (action is LoadRealtorName) -> {
                mutate(state().copy(realtorName = action.name, loading = false))
            }
            (action is EmailRealtor) -> {
                launchEmailClient(action.toAddress, action.fromAddress, action.subject)
            }
            else -> { throw RuntimeException(action.toString()) }
        }
    }

    private fun confirmAndDeleteProperty(propId: String) {
        fragment.confirmAndDo("Delete " + state().address, "Are you sure?  This can't be undone.") {
            repo.deleteProp(propId)
            fragment.makeToast(state().address + " deleted.")
            Rudder.navBack()
        }
    }

    private fun loadProperty(prop: Prop) {
        mutate(state().copy(
            loaded = true,
            coordx = prop.coordx,
            coordy = prop.coordy,
            name = prop.name,
            desc = prop.desc,
            sqft = prop.sqft,
            rent = prop.rent,
            rooms = prop.rooms,
            addtime = prop.addtime,
            available = prop.available,
            realtorId = prop.realtorId,
            address = prop.address,
            city = prop.city,
            state = prop.state,
            zip = prop.zip
        ))
        repo.getUser(prop.realtorId) {
            actions.onNext(LoadRealtorName(it?.username ?: "none"))
        }
    }

    private fun propFromState(): Prop {
        val state = state()
        val prop = Prop(
            id = state.propId,
            coordx = state.coordx,
            coordy = state.coordy,
            name = state.name,
            desc = state.desc,
            sqft = state.sqft,
            rent = state.rent,
            rooms = state.rooms,
            addtime = state.addtime,
            available = state.available,
            realtorId = state.realtorId,
            address = state.address,
            city = state.city,
            state = state.state,
            zip = state.zip
        )
        return prop
    }

    private fun updateProperty(property: Prop) {
        repo.putProp(property)
        fragment.makeToast("Property is now " + (if (property.available) "available" else "unavailable") + ".")
    }

    private fun launchEmailClient(toAddress: String, fromAddress: String, subject: String) {
        // this is a hack.  what is the right way for a presenter to signal back to the activity to do Android things?
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(toAddress))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.setType("message/rfc822")
        fragment.activity?.startActivity(Intent.createChooser(intent, "Send email using:"))
    }
}