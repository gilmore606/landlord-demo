package com.dlfsystems.landlord

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.dlfsystems.landlord.data.model.User
import com.dlfsystems.landlord.nav.FragmentStateChanger
import com.dlfsystems.landlord.nav.Rudder
import com.dlfsystems.landlord.screens.base.BaseKey
import com.dlfsystems.landlord.screens.login.LoginKey
import com.dlfsystems.landlord.screens.propdetail.PropdetailKey
import com.dlfsystems.landlord.screens.proplist.ProplistKey
import com.dlfsystems.landlord.screens.propmap.PropmapKey
import com.dlfsystems.landlord.screens.userlist.UserlistKey
import com.google.android.material.navigation.NavigationView
import com.zhuinden.simplestack.BackstackDelegate
import com.zhuinden.simplestack.History
import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestack.StateChanger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity(), StateChanger, NavigationView.OnNavigationItemSelectedListener {

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

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Landlord"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        nav_view.setNavigationItemSelectedListener(this)

        fragmentStateChanger = FragmentStateChanger(supportFragmentManager, R.id.base_frame)
        backstackDelegate.setStateChanger(this)

        disposables += Rudder.navDest.observeOn(AndroidSchedulers.mainThread())
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

    fun toggleToolbar(showToolbar: Boolean) {
        if (showToolbar and !(supportActionBar?.isShowing ?: true)) {
            supportActionBar?.show()
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else if (!showToolbar and (supportActionBar?.isShowing ?: false)) {
            supportActionBar?.hide()
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (!backstackDelegate.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun handleStateChange(stateChange: StateChange, completionCallback: StateChanger.Callback) {
        if (stateChange.isTopNewStateEqualToPrevious) {
            Timber.d("RUDDER statechange was same as previous")
            completionCallback.stateChangeComplete()
            return
        }
        fragmentStateChanger.handleStateChange(stateChange)
        completionCallback.stateChangeComplete()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_proplist -> {
                Rudder.navTo(ProplistKey())
            }
            R.id.nav_item_propmap -> {
                Rudder.navTo(PropmapKey())
            }
            R.id.nav_item_propadd -> {
                Rudder.navTo(PropdetailKey(""))
            }
            R.id.nav_item_userlist -> {
                Rudder.navTo(UserlistKey())
            }
            R.id.nav_item_logout -> {
                logOut()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun onLogin(user: User, password: String) {
        Prefs(this).loginUser = user.username
        Prefs(this).loginPassword = password
        Prefs(this).userId = user.uid

        nav_view.menu.findItem(R.id.nav_item_propadd).setEnabled(user.isRealtor)
        nav_view.menu.findItem(R.id.nav_item_userlist).setEnabled(user.isAdmin)
    }

    private fun logOut() {
        Prefs(this).userId = ""
        Rudder.navTo(LoginKey())
    }
}
