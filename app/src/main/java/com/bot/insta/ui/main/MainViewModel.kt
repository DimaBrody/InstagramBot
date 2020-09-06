package com.bot.insta.ui.main

import com.bot.insta.components.mvvm.viewmodel.ViewModel
import com.bot.insta.network.source.NetworkDataSource
import com.bot.insta.tools.prefs.PreferenceProvider

class MainViewModel(
    private val prefs: PreferenceProvider,
    private val networkDataSource: NetworkDataSource
) : ViewModel(){


}