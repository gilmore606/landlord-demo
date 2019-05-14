package com.dlfsystems.landlord

import android.content.Context
import android.preference.PreferenceManager
import com.dlfsystems.landlord.data.model.PropFilter
import com.dlfsystems.landlord.data.model.User
import com.google.gson.Gson

class Prefs(context: Context) {

    companion object {
        val LOGIN_USER = "com.dlfsystems.landlord.LOGIN_USER"
        val LOGIN_PASSWORD = "com.dlfsystems.landlord.LOGIN_PASSWORD"
        val USER = "com.dlfsystems.landlord.USER"
        val SEARCH_FILTER = "com.dlfsystems.landlord.SEARCH_FILTER"

        val gson = Gson()
    }

    val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var loginUser
        get() = preferences.getString(LOGIN_USER, "")
        set(value) = preferences.edit().putString(LOGIN_USER, value).apply()

    var loginPassword
        get() = preferences.getString(LOGIN_PASSWORD, "")
        set(value) = preferences.edit().putString(LOGIN_PASSWORD, value).apply()

    var user: User?
        get() {
            val json = preferences.getString(USER, "")
            if (json == "") return null
            else return gson.fromJson(json, User::class.java)
        }
        set(value) {
            preferences.edit().putString(USER, gson.toJson(value)).apply()
        }

    var searchFilter: PropFilter
        get() {
            val json = preferences.getString(SEARCH_FILTER, "")
            if (json == "") return PropFilter()
            else return gson.fromJson(json, PropFilter::class.java)
        }
        set(value) {
            preferences.edit().putString(SEARCH_FILTER, gson.toJson(value)).apply()
        }
}