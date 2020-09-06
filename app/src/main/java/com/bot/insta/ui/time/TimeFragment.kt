package com.bot.insta.ui.time

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bot.insta.R
import com.bot.insta.components.dependencies.prefs
import com.bot.insta.components.navigation.Navigation
import com.bot.insta.components.navigation.navigateMain
import com.bot.insta.internal.setOnTextWatcher
import kotlinx.android.synthetic.main.fragment_time.view.*
import java.util.concurrent.TimeUnit

class TimeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_time, container, false)

        view.initViews()

        return view
    }

    private fun View.initViews() {
        toolbar_time.setNavigationIcon(R.drawable.ic_close)
        toolbar_time.navigationIcon?.setColorFilter(
            resources.getColor(R.color.grey_60),
            PorterDuff.Mode.SRC_ATOP
        )
        toolbar_time.title = "Time"
        toolbar_time.setOnClickListener {
            Navigation.processBack(::navigateMain)
        }

        time_edit_following.setText(
            TimeUnit.MILLISECONDS
                .toMinutes(prefs.currentFollowingTime).toString()
        )

        time_edit_following.setOnTextWatcher {
            if(!it.isNullOrEmpty()){
                prefs.currentFollowingTime =
                    TimeUnit.MINUTES.toMillis(it.toString().toLong())
            }
        }

        time_edit_followers.setText(
            TimeUnit.MILLISECONDS
                .toHours(prefs.currentFollowersTime).toString()
        )

        time_edit_followers.setOnTextWatcher {
            if(!it.isNullOrEmpty()){
                prefs.currentFollowersTime =
                    TimeUnit.HOURS.toMillis(it.toString().toLong())
            }

        }

        time_edit_count.setText(prefs.peopleCount.toString())

        time_edit_count.setOnTextWatcher {
            if(!it.isNullOrEmpty()){
                prefs.peopleCount = it.toString().toInt()
            }
        }

    }

}