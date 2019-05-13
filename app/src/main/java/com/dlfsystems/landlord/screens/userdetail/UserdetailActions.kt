package com.dlfsystems.landlord.screens.userdetail

import com.dlfsystems.landlord.data.model.User
import com.dlfsystems.landlord.screens.base.Action

class LoadUserAction(
    val userid: String
) : Action

class UserLoadedAction(
    val user: User
) : Action

class UserChangeRealtorAction(
    val value: Boolean
) : Action

class UserChangeAdminAction(
    val value: Boolean
) : Action

class UserSaveChanges(
    val user: User
) : Action