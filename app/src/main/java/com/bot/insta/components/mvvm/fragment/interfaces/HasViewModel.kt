package com.bot.insta.components.mvvm.fragment.interfaces

import com.bot.insta.components.mvvm.viewmodel.ViewModel
import kotlin.reflect.KClass

interface HasViewModel<VM: ViewModel> {
    val mViewModel : VM

    val viewModelClass: KClass<VM>
}