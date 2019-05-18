package com.dlfsystems.landlord.screens.userdetail

import com.dlfsystems.landlord.data.model.User
import com.dlfsystems.landlord.screens.base.Action

class UserLoaded(
    val user: User
) : Action

class UserChangeRealtor(
    val value: Boolean
) : Action

class UserChangeAdmin(
    val value: Boolean
) : Action

class UserSaveChanges(
    val user: User
) : Action

class UserDelete(
    val user: User
) : Action