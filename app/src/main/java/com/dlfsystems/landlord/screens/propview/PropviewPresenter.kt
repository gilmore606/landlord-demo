package com.dlfsystems.landlord.screens.propview

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
            loading = false,
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
    }
}