package com.bot.insta.internal.access

import android.content.Context
import android.view.accessibility.AccessibilityNodeInfo
import com.bot.insta.internal.NoNodeException
import com.bot.insta.internal.click
import com.bot.insta.internal.log
import com.bot.insta.tools.services.accessibility.UtilsService

@Throws(NoNodeException::class)
fun clickNode(
    context: Context
): AccessibilityNodeInfo {
    val window = UtilsService.instance!!.rootInActiveWindow
    return window.clickNode(context)
}

private fun AccessibilityNodeInfo.clickNode(
    context: Context
): AccessibilityNodeInfo {

    click()

    log("$viewIdResourceName $text $className")

    for (i in 0 until childCount)
        try {
            return getChild(i).clickNode(context)
        } catch (e: NoNodeException) {
        } catch (e: IllegalStateException) {
        }

    throw NoNodeException()
}