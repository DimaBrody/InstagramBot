@file:JvmName("NavigationKt")

package com.bot.insta.components.navigation

import com.bot.insta.ui.login.LoginFragment
import com.bot.insta.ui.main.MainFragment
import com.bot.insta.ui.names.NamesFragment
import com.bot.insta.ui.time.TimeFragment

//import com.pep.utility.ui.overlay.OverlayFragment
fun navigateMain() {
    Navigation.navigate(MainFragment())
}

fun navigateLogin(){
    Navigation.navigate(LoginFragment())
}

fun navigateNames(){
    Navigation.navigate(NamesFragment())
}

fun navigateTime(){
    Navigation.navigate(TimeFragment())
}