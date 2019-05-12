package com.dlfsystems.landlord

import android.content.Context
import android.preference.PreferenceManager
import com.dlfsystems.landlord.data.model.PropFilter
import com.google.gson.Gson

class Prefs(context: Context) {

    companion object {
        val LOGIN_USER = "com.dlfsystems.landlord.LOGIN_USER"
        val LOGIN_PASSWORD = "com.dlfsystems.landlord.LOGIN_PASSWORD"
        val USERID = "com.dlfsystems.landlord.USERID"
        val SEARCH_FILTER = "com.dlfsystems.landlord.SEARCH_FILTER"
    }

    val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var loginUser
        get() = preferences.getString(LOGIN_USER, "")
        set(value) = preferences.edit().putString(LOGIN_USER, value).apply()

    var loginPassword
        get() = preferences.getString(LOGIN_PASSWORD, "")
        set(value) = preferences.edit().putString(LOGIN_PASSWORD, value).apply()

    var userId
        get() = preferences.getString(USERID, "")
        set(value) = preferences.edit().putString(USERID, value).apply()

    var searchFilter: PropFilter
        get() {
            val json = preferences.getString(SEARCH_FILTER, "")
            if (json == "") return PropFilter()
            else return Gson().fromJson(json, PropFilter::class.java)
        }
        set(value) {
            preferences.edit().putString(SEARCH_FILTER, Gson().toJson(value)).apply()
        }
}