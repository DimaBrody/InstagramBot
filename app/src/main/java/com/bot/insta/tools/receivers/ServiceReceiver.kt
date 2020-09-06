package com.bot.insta.tools.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bot.insta.components.dependencies.prefs
import com.bot.insta.internal.access.checkAccessibilityPermission
import com.bot.insta.internal.safeApply
import com.bot.insta.internal.scheduleBot
import com.bot.insta.internal.stopSchedule
import com.bot.insta.tools.services.UnFollowingService
import com.bot.insta.tools.services.accessibility.ModeService
import com.bot.insta.tools.services.accessibility.ModeService.Companion.STRATEGY_EXTRA
import com.bot.insta.tools.services.accessibility.ModeService.Companion.SUBSCIBE_STRATEGY

class ServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context.safeApply {
            scheduleBot(this)
            if (prefs.isLoggedIn)
                startService(Intent(context, UnFollowingService::class.java))
            else {
                checkAccessibilityPermission(
                    this,
                    {}) {
                    if (prefs.isAutomatic)
                        startModeService(this)
                    else stopSchedule(context!!)
                }
            }
        }
    }

    companion object {
        fun startModeService(context: Context){
            context.startService(Intent(context, ModeService::class.java).apply {
                putExtra(STRATEGY_EXTRA,SUBSCIBE_STRATEGY)
            })
        }
    }
}