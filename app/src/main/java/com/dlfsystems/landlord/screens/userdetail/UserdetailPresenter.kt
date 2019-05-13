package com.dlfsystems.landlord.screens.userdetail

import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.screens.base.Action
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BasePresenter
import java.lang.RuntimeException

class UserdetailPresenter(fragment: BaseFragment) : BasePresenter(fragment) {

    fun state() = stateHolder.state.value as UserdetailState

    override fun hearAction(action: Action) {
        when {
            (action is LoadUserAction) -> {
                mutate(state().copy(loading = true))
                FirebaseRepository().getUser(action.userid) {
                    fragment.actions.onNext(
                        UserLoadedAction(it)
                    )
                }
            }
            (action is UserLoadedAction) -> {
                mutate(state().copy(loading = false,
                    username = action.user.username,
                    isAdmin = action.user.isAdmin,
                    isRealtor = action.user.isRealtor))
            }
            else -> { throw RuntimeException(action.toString()) }
        }
    }
}