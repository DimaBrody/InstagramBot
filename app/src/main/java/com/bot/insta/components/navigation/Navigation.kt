package com.bot.insta.components.navigation

import android.util.Log
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.bot.insta.R

import com.bot.insta.internal.safeApply
import com.bot.insta.ui.login.LoginFragment
import com.bot.insta.ui.main.MainFragment

object Navigation {
    private const val NO_INIT = "Write Navigation." +
            "init(this) in your activity!"

    private var activity: FragmentActivity? = null

    private val transactionAnimations =
        TransactionAnimations.create()

    private var container: Int = 0

    private fun safeSplash(onSplash: (() -> Fragment)?) {
        onSplash?.let { navigate(it.invoke()) }
    }

    fun init(
        activity: FragmentActivity,
        container: Int,
        onSignedIn: (() -> Fragment)? = null,
        onUnsignedIn: (() -> Fragment)? = null
    ) {
        Navigation.activity = activity
        Navigation.container = container

//        if(prefs.isLoggedIn){
//            startInstagram({
//                onUnsignedIn?.let { navigate(it.invoke()) }
//            }){
//                onSignedIn?.let { navigate(it.invoke()) }
//            }
//        }  else onUnsignedIn?.let { navigate(it.invoke()) }

        onUnsignedIn?.let { navigate(it.invoke()) }
        onSignedIn?.let { navigate(it.invoke()) }

        setNavigationLifecycleObserver(activity)
    }

    fun navigate(
        fragment: Fragment,
        isAnimate: Boolean = true,
        isEnterOffset: Boolean = true
    ) = activity?.supportFragmentManager?.safeApply {
        val currentFragment = findFragmentById(container)
        //if(currentFragment.name() != fragment.name())

        val transition = if (isAnimate) beginAnimatedTransaction {
            enter = if (isEnterOffset) R.anim.nav_default_enter_anim else R.anim.fade_anim
            exit = R.anim.nav_default_exit_anim
            popEnter = R.anim.nav_default_pop_enter_anim
            popExit = R.anim.nav_default_pop_exit_anim
        } else beginTransaction()


        transition.replace(container, fragment, fragment.name())
            .commitAllowingStateLoss()
    } ?: also {
        if (activity == null)
            Log.e(name(), NO_INIT)
    }

    fun processBack(onSuccess: () -> Unit): Boolean =
        activity?.safeApply {
            with(supportFragmentManager) {
                val fragment = findFragmentById(container)
                if (fragment !is MainFragment && fragment !is LoginFragment && fragment != null) {
                    onSuccess()
                    true
                } else false
            }
        } ?: false

    private fun setNavigationLifecycleObserver(
        owner: LifecycleOwner
    ) {
        owner.lifecycle.addObserver(NavigationLifecycleObserver())
    }

    private fun FragmentManager.beginAnimatedTransaction(
        onSetAnimations: TransactionAnimations.() -> Unit
    ): FragmentTransaction {
        onSetAnimations(transactionAnimations)
        return with(transactionAnimations) {
            beginTransaction().setCustomAnimations(
                enter.normal(),
                exit.normal(),
                popEnter.normal(),
                popExit.normal()
            )
        }
    }

    private class TransactionAnimations {
        @AnimRes
        @AnimatorRes
        var enter: Int? = null
        @AnimRes
        @AnimatorRes
        var exit: Int? = null
        @AnimRes
        @AnimatorRes
        var popEnter: Int? = null
        @AnimRes
        @AnimatorRes
        var popExit: Int? = null

        companion object {
            fun create(): TransactionAnimations =
                TransactionAnimations()
        }
    }

    private class NavigationLifecycleObserver : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
//            if (event == Lifecycle.Event.ON_DESTROY)
//                activity = null
        }
    }


    private fun Int?.normal() = this ?: 0

    fun Any?.name() = this?.javaClass?.simpleName ?: "null"
}