package com.dlfsystems.landlord.nav

import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseKey
import com.zhuinden.simplestack.StateChange
import timber.log.Timber


class FragmentStateChanger(
    private val fragmentManager: androidx.fragment.app.FragmentManager,
    private val containerId: Int
) {

    class FragWithTag(val fragment: BaseFragment, val tag: String)

    fun handleStateChange(stateChange: StateChange) {
        Timber.d("RUDDER state change")
        val removeList = ArrayList<BaseFragment>(0)
        val addList = ArrayList<FragWithTag>(0)
        val showList = ArrayList<BaseFragment>(0)
        val hideList = ArrayList<BaseFragment>(0)

        val previousState = stateChange.getPreviousState<BaseKey>()
        val newState = stateChange.getNewState<BaseKey>()

        val animationSet = if (stateChange.direction == StateChange.FORWARD)
            stateChange.topNewState<BaseKey>().getAnimation()
        else
            stateChange.topPreviousState<BaseKey>()?.getBackAnimation() ?: FragAnimPair(0, 0)

        previousState.forEach { oldKey ->
            fragmentManager.findFragmentByTag(oldKey.fragmentTag)?.also {
                if (!newState.contains(oldKey)) {
                    removeList.add(it as BaseFragment)
                } else if (!it.isHidden) {
                    hideList.add(it as BaseFragment)
                }
            }
        }
        newState.forEach { newKey ->
            var fragment: androidx.fragment.app.Fragment? = fragmentManager.findFragmentByTag(newKey.fragmentTag)
            if (newKey == stateChange.topNewState<Any>()) {
                if (fragment != null) {
                    if (fragment.isHidden) showList.add(fragment as BaseFragment)
                } else {
                    fragment = newKey.newFragment()
                    addList.add(FragWithTag(fragment, newKey.fragmentTag))
                }
            } else {
                if (fragment != null && !fragment.isHidden) {
                    hideList.add(fragment as BaseFragment)
                }
            }
        }

        fragmentManager.beginTransaction().disallowAddToBackStack().apply {
            setCustomAnimations(animationSet.animIn, animationSet.animOut)
            removeList.forEach { remove(it)
                Timber.d("RUDDER remove " + it.tag) }
            addList.forEach { add(containerId, it.fragment, it.tag)
                Timber.d("RUDDER add " + it.tag) }
            showList.forEach { show(it)
                Timber.d("RUDDER show " + it.tag) }
            hideList.forEach { hide(it)
                Timber.d("RUDDER hide " + it.tag) }
        }.commitNow()
        fragmentManager.executePendingTransactions()

        showList.forEach { it.onShowFromBackStack() }
    }
}
