package com.bot.insta.ui.main

import android.view.View
import com.bot.insta.App
import com.bot.insta.components.dependencies.prefs
import com.bot.insta.components.navigation.navigateNames
import com.bot.insta.components.navigation.navigateTime
import com.bot.insta.internal.scheduleInstant
import com.bot.insta.tools.receivers.ServiceReceiver
import com.bot.insta.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : BaseFragment<MainViewModel>(
    MainViewModel::class
) {

    override fun View.createViews() {
        setupDefaults()
        setupObservers()
    }

    private fun View.setupDefaults() {
        main_button_search.setOnClickListener {
            main_button_search.isEnabled = false

            //We start our service here, it schedules and works every amount of time
            scheduleInstant(requireContext())


            App.handler?.postDelayed({
                main_button_search.isEnabled = true
            }, 2000)
        }

        main_button_people.setOnClickListener {
            navigateNames()
        }

        main_button_time.setOnClickListener {
            navigateTime()
        }

        if(prefs.isLoggedIn){
            main_button_login.visibility = View.INVISIBLE
        } else
            main_username.visibility = View.INVISIBLE

//        Picasso.get().load(prefs.urlAvatar).into(main_image_avatar)

        main_username.text = prefs.username
    }

    private fun View.setupObservers() {

    }

}