package com.bot.insta.internal.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.bot.insta.components.dependencies.prefs
import com.bot.insta.internal.NoNodeException
import com.bot.insta.internal.access.checkNode
import com.bot.insta.internal.access.clickNode
import com.bot.insta.internal.safeApply



class BotReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent.safeApply {
            when(action){
                CLEAR_PREFS->{
                    prefs.username = ""
                    prefs.password = ""
                    prefs.isLoggedIn = false
                }
                checkNodesName->{
                    try {
                        checkNode(context!!)
                    } catch (e: NoNodeException){
                        e.printStackTrace()
                    } catch (e: Exception){
                        e.printStackTrace()
                    }
                }
                clickNodesName->{
                    try {
                        clickNode(context!!)
                    } catch (e: NoNodeException){
                        // e.printStackTrace()
                    } catch (e: Exception){
                        // e.printStackTrace()
                    }
                }
                enableAutoStart->
                    prefs.isLaunchOnStart = true
                disableAutoStart->
                    prefs.isLaunchOnStart = false
                else->{}
            }
        }
    }

    companion object {
        private const val CLEAR_PREFS = "prefs.clear"

        private const val checkNodesName = "checkNodes"

        private const val clickNodesName = "clickNodes"

        private const val enableAutoStart = "startOn"

        private const val disableAutoStart = "startOff"

        fun register(context: Context){
            context.registerReceiver(BotReceiver(), IntentFilter().apply {
                addAction(CLEAR_PREFS)
                addAction(checkNodesName)
                addAction(clickNodesName)
                addAction(enableAutoStart)
                addAction(disableAutoStart)
            })
        }
    }

}