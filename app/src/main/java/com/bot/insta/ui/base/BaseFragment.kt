package com.bot.insta.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bot.insta.R
import com.bot.insta.components.mvvm.fragment.ViewModelFragment
import com.bot.insta.components.mvvm.fragment.interfaces.HasViewModel
import com.bot.insta.components.mvvm.viewmodel.ViewModel
import com.bot.insta.components.mvvm.viewmodel.delegates.viewModelDelegate
import com.bot.insta.ui.MainActivity
import com.bot.insta.ui.base.BaseFragment.FragmentType.LOGIN
import com.bot.insta.ui.base.BaseFragment.FragmentType.MAIN
import com.bot.insta.ui.base.BaseFragment.FragmentType.UNDEFINED
import com.bot.insta.ui.login.LoginViewModel
import com.bot.insta.ui.main.MainViewModel

import kotlin.reflect.KClass

abstract class BaseFragment<VM : ViewModel>(
    final override val viewModelClass: KClass<VM>
) : ViewModelFragment(), HasViewModel<VM> {

    private var mFragmentType: Int = UNDEFINED

    private val mainActivity: MainActivity?
        get() = (activity as? MainActivity?)

    override val mViewModel: VM
            by viewModelDelegate(viewModelClass)

    protected val fragment: Fragment
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFragmentType()
    }

    private fun setupFragmentType(){
        mFragmentType = with(viewModelClass.java){
            when {
                isAssignableFrom(MainViewModel::class.java) -> MAIN
                isAssignableFrom(LoginViewModel::class.java) -> LOGIN
                else-> UNDEFINED
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutID,container,false)

        view.createViews()

        return view
    }

    private val layoutID: Int
        get() = when (mFragmentType) {
            MAIN -> R.layout.fragment_main
            LOGIN -> R.layout.fragment_login
            else -> throw IllegalArgumentException(
                "No Layout for current fragment found!"
            )
        }


    abstract fun View.createViews()

    private object FragmentType {
        const val MAIN = 5
        const val LOGIN = 6
        const val UNDEFINED = 8

    }

}