package com.bot.insta.ui

import android.content.Intent
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bot.insta.R
import com.bot.insta.components.navigation.Navigation
import com.bot.insta.components.navigation.navigateMain
import com.bot.insta.data.variables.AppValues
import com.bot.insta.internal.access.checkAccessibilityPermission
import com.bot.insta.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Navigation.init(this, R.id.main_host, onSignedIn, onUnsignedIn)

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        AppValues.screenDimensions[0] = size.x
        AppValues.screenDimensions[1] = size.y
    }

    override fun onStart() {
        super.onStart()

        checkAccessibilityPermission(
            this@MainActivity,
            {
                //                    if (prefs.isLaunchOnStart)
//                        startService(Intent(this, ModeService::class.java))
            },
            false
        ) {
            startAutoAccessibility()
        }


    }

    private fun startAutoAccessibility() {
        val intent = Intent("do.it.for.me")
        sendBroadcast(intent)
    }

    private val onSignedIn: () -> Fragment
        get() = { MainFragment() }

    private val onUnsignedIn: () -> Fragment
        get() = { MainFragment() }

    override fun onBackPressed() {
        if (!Navigation.processBack(::navigateMain)) finish()
    }
}
