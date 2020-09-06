package com.bot.insta.components.mvvm.fragment

import androidx.fragment.app.Fragment
import com.bot.insta.components.mvvm.fragment.interfaces.HasViewModelStore

abstract class ViewModelFragment : Fragment(), HasViewModelStore {

    private val _store: ViewModelStore =
        ViewModelStore()

    override val store: ViewModelStore
        get() = _store

}