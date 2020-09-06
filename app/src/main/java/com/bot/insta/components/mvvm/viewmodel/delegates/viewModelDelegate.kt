package com.bot.insta.components.mvvm.viewmodel.delegates

import com.bot.insta.components.mvvm.viewmodel.ViewModel
import com.bot.insta.components.mvvm.viewmodel.ViewModelProvider
import kotlin.reflect.KClass

fun <V : ViewModel> viewModelDelegate(clazz: KClass<V>) =
    ViewModelProvider.ViewModelDelegate(clazz)