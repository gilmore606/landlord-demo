package com.dlfsystems.landlord

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.dlfsystems.landlord.nav.FragmentStateChanger
import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.screens.base.BaseKey
import com.dlfsystems.landlord.screens.login.LoginKey
import com.zhuinden.simplestack.BackstackDelegate
import com.zhuinden.simplestack.History
import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestack.StateChanger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MainActivity : AppCompatActivity(), StateChanger {

    lateinit var backstackDelegate: BackstackDelegate
    lateinit var fragmentStateChanger: FragmentStateChanger
    var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        backstackDelegate = BackstackDelegate()
        backstackDelegate.onCreate(savedInstanceState,
            lastCustomNonConfigurationInstance,
            History.single(LoginKey()))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentStateChanger = FragmentStateChanger(supportFragmentManager, R.id.base_frame)
        backstackDelegate.setStateChanger(this)

        disposables += Rudder.navDest.distinctUntilChanged().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                navigateTo(it)
            }
    }

    override fun onRetainCustomNonConfigurationInstance(): Any? {
        return backstackDelegate.onRetainCustomNonConfigurationInstance()
    }

    private fun navigateTo(destKey: BaseKey) {
        hideKeyboard()
        backstackDelegate.backstack.goTo(destKey)
        if (!destKey.allowBack)
            backstackDelegate.backstack.setHistory(History.single(destKey), StateChange.REPLACE)
    }

    override fun onBackPressed() {
        if (!backstackDelegate.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun handleStateChange(stateChange: StateChange, completionCallback: StateChanger.Callback) {
        if (stateChange.isTopNewStateEqualToPrevious) {
            completionCallback.stateChangeComplete()
            return
        }
        fragmentStateChanger.handleStateChange(stateChange)
        completionCallback.stateChangeComplete()
    }
}
