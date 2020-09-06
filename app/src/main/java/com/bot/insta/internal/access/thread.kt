package com.bot.insta.internal.access

import com.bot.insta.App
import com.bot.insta.internal.PACKAGE_SETTINGS
import com.bot.insta.internal.log
import com.bot.insta.tools.services.accessibility.UtilsService

private const val ACTION_DELAY = 240L
private const val TAG = "Accessibility"

val currentTime: Long
    get() = System.currentTimeMillis()


enum class LoopState {
    Continue, Break
}

fun post(delay: Long = 0, action: () -> Unit) {
    App.handler!!.postDelayed(action, delay)
}

private fun iterateDelayed(onComplete: () -> LoopState) {
    lateinit var next: () -> Unit
    next = {
        if (onComplete() == LoopState.Continue)
            post(
                ACTION_DELAY,
                next
            )
    }
    post(
        ACTION_DELAY,
        next
    )
}

fun iterateDelayed(timeout: Long, onTimeout: () -> Unit, onComplete: () -> LoopState) {
    val mTime = currentTime
    iterateDelayed {
        if (currentTime - mTime > timeout) {
            onTimeout()
            LoopState.Break
        } else onComplete()
    }
}

fun iterateConditionally(
    screen: Boolean,
    timeout: Long,
    onTimeOut: () -> Unit,
    onComplete: () -> Unit
) {
    iterateDelayed(
        timeout, onTimeOut, {
            if (isWindowOpened) {
                onComplete()
                LoopState.Break
            } else LoopState.Continue
        })
}


private val isWindowOpened: Boolean
    get() {
        val currentWindow = UtilsService
            .instance!!.rootInActiveWindow
        return if (currentWindow == null) {
//            Log.d("EAZY", "Current Window is Null")
            false
        } else {
            val packageName = currentWindow.packageName.toString()
//            log(packageName)
            packageName == PACKAGE_SETTINGS
        }
    }
