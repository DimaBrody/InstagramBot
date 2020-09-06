package com.bot.insta.components.mvvm.fragment

import com.bot.insta.components.mvvm.viewmodel.ViewModel


class ViewModelStore {

    private val mStore = hashMapOf<String, ViewModel>()

    operator fun set(name: String,viewModel: ViewModel){
        mStore[name] = viewModel
    }

    operator fun get(name: String) : ViewModel? =
        mStore[name]

    fun clear(){
        for(vm in mStore.values)
            vm.clear()
    }

}