package com.dlfsystems.landlord.screens.userdetail

import com.dlfsystems.landlord.data.model.User
import com.dlfsystems.landlord.screens.base.Action

class UserLoaded(
    val user: User
) : Action

class SetRealtorStatus(
    val value: Boolean
) : Action

class SetAdminStatus(
    val value: Boolean
) : Action

class SubmitUser(
    val user: User
) : Action

class DeleteUser(
    val user: User
) : Action