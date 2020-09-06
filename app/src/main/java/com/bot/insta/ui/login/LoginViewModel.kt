package com.bot.insta.ui.login

import androidx.lifecycle.LiveData
import com.bot.insta.components.mvvm.viewmodel.ViewModel
import com.bot.insta.data.request.payload.InstagramLoginResult
import com.bot.insta.network.source.NetworkDataSource
import com.bot.insta.tools.prefs.PreferenceProvider

class LoginViewModel(
    private val prefs: PreferenceProvider,
    private val networkDataSource: NetworkDataSource
) : ViewModel() {

    fun requestLogin(
        isFacebook: Boolean,
        username: String,
        password: String
    ) {
        prefs.username = username
        prefs.password = password

        networkDataSource.loginRequest(isFacebook)
    }

    val loginData: LiveData<InstagramLoginResult>
        get() = networkDataSource.loginData

}