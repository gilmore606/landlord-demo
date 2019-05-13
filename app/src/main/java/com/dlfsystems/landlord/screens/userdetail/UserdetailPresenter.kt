package com.dlfsystems.landlord.screens.userdetail

import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.data.model.User
import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter
import com.dlfsystems.landlord.screens.userlist.UserlistKey
import java.lang.RuntimeException

class UserdetailPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    fun state() = stateHolder.state.value as UserdetailState

    val repo = FirebaseRepository()

    override fun hearAction(action: Action) {
        when {
            (action is LoadUser) -> {
                mutate(state().copy(loading = true))
                FirebaseRepository().getUser(action.userid) {
                    fragment.actions.onNext(
                        UserLoaded(it)
                    )
                }
            }
            (action is UserLoaded) -> {
                mutate(state().copy(loading = false,
                    username = action.user.username,
                    isAdmin = action.user.isAdmin,
                    isRealtor = action.user.isRealtor))
            }
            (action is UserChangeRealtor) -> {
                mutate(state().copy(isRealtor = action.value))
            }
            (action is UserChangeAdmin) -> {
                mutate(state().copy(isAdmin = action.value))
            }
            (action is UserSaveChanges) -> {
                FirebaseRepository().putUser(action.user)
                fragment.makeToast(action.user.username + " updated.")
                Rudder.navTo(UserlistKey())
            }
            (action is UserDelete) -> {
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
            Rudder.navTo(UserlistKey())
        }
    }
}