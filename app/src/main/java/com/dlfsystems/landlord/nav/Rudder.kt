package com.dlfsystems.landlord.nav

import com.dlfsystems.landlord.screens.base.BaseKey
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Emitter
import io.reactivex.observables.ConnectableObservable
import timber.log.Timber

object Rudder {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var navDestEmitter: Emitter<BaseKey>

    val navDest: ConnectableObservable<BaseKey> = ConnectableObservable.create<BaseKey> {
        navDestEmitter = it
    }.publish()

    init {
        navDest.connect()
    }

    fun navTo(dest: BaseKey) {
        Timber.d("RUDDER navTo " + dest.toString())
        navDestEmitter.onNext(dest)
    }

    fun navBack() {
        Timber.d("RUDDER navBack")
        navDestEmitter.onNext(BackKey())
    }

}