package com.bot.insta

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.bot.insta.components.dependencies.dependencies
import com.bot.insta.components.dependencies.prefs
import com.bot.insta.internal.receivers.BotReceiver
import com.bot.insta.tools.delegates.AppActivityLifecycle
import com.bot.insta.tools.notifications.NotificationProvider

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        dependencies { this }

        handler = Handler(Looper.getMainLooper())

        NotificationProvider.init(this)

        BotReceiver.register(this)

        registerActivityLifecycleCallbacks(activityLifecycleCallback)
    }


    companion object {
        var handler: Handler? = null
            private set

        val activityLifecycleCallback = AppActivityLifecycle()

        var listOfAccounts: List<String>
            get() = prefs.currentList
            set(value) {
                prefs.currentList = value
            }

        var listOfHashtags: List<String>
            get() = prefs.hashtagList
            set(value) {
                prefs.hashtagList = value
            }


        const val CHANNEL_ID = BuildConfig.APPLICATION_ID
    }


}