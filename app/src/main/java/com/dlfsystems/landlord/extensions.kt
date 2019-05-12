package com.dlfsystems.landlord

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.Executors

operator fun CompositeDisposable.plusAssign(subscription: Disposable) {
    add(subscription)
}

fun prefs(context: Context) : SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(context)
}

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

fun ioThread(f : () -> Unit) {
    IO_EXECUTOR.execute(f)
}