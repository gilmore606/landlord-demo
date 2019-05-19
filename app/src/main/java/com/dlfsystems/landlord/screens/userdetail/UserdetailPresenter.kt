package com.dlfsystems.landlord.screens.userdetail

import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.data.model.User
import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter
import java.lang.RuntimeException

class UserdetailPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    fun state() = stateHolder.state.value as UserdetailState

    val repo = FirebaseRepository()

    override fun hearAction(action: Action) {
        when {
            (action is InitialState) -> {
                val state = state()
                if ((state.username == "") and (!state.loading)) {
                    mutate(state().copy(loading = true))
                    FirebaseRepository().getUser(state.userId) {
                        actions.onNext(UserLoaded(it))
                    }
                }
            }
            (action is UserLoaded) -> {
                mutate(state().copy(loading = false,
                    username = action.user.username,
                    isAdmin = action.user.isAdmin,
                    isRealtor = action.user.isRealtor))
            }
            (action is SetRealtorStatus) -> {
                mutate(state().copy(isRealtor = action.value))
            }
            (action is SetAdminStatus) -> {
                mutate(state().copy(isAdmin = action.value))
            }
            (action is SubmitUser) -> {
                FirebaseRepository().putUser(action.user)
                fragment.makeToast(action.user.username + " updated.")
                Rudder.navBack()
            }
            (action is DeleteUser) -> {
                confirmAndDeleteUser(action.user)
            }
            else -> { throw RuntimeException(action.toString()) }
        }
    }

    private fun confirmAndDeleteUser(user: User) {
        fragment.confirmAndDo("Delete " + user.username, "Are you sure?  This can't be undone.") {
            //Rudder.auth.deleteUser(user.uid)
            repo.deleteUser(user.uid)
            fragment.makeToast(user.username + " deleted.")
            Rudder.navBack()
        }
    }
}