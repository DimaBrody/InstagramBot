package com.bot.insta.components.factory

import com.bot.insta.components.mvvm.factory.Factory
import com.bot.insta.network.source.NetworkDataSource
import com.bot.insta.tools.prefs.PreferenceProvider
import com.bot.insta.ui.login.LoginViewModel
import com.bot.insta.ui.main.MainViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val prefs: PreferenceProvider,
    private val networkDataSource: NetworkDataSource
) : Factory {

    override fun <F> create(modelClass: Class<F>): F = with(modelClass) {
        when {
            isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                prefs,
                networkDataSource
            ) as F
            isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                prefs,
                networkDataSource
            ) as F
            else -> throw IllegalArgumentException("Wrong ViewModel.")
        }
    }

}