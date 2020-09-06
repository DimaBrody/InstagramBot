package com.bot.insta.internal.access

import android.content.Context
import android.view.accessibility.AccessibilityNodeInfo
import com.bot.insta.internal.NoNodeException
import com.bot.insta.internal.log
import com.bot.insta.tools.services.accessibility.UtilsService

@Throws(NoNodeException::class)
fun checkNode(
    context: Context
): AccessibilityNodeInfo {
    val window = UtilsService.instance!!.rootInActiveWindow
    return window.checkNode(context)
}

private fun AccessibilityNodeInfo.checkNode(
    context: Context
): AccessibilityNodeInfo {

    log("$className $viewIdResourceName $text ${this.contentDescription}")

    for (i in 0 until childCount)
        try {
            return getChild(i).checkNode(context)
        } catch (e: NoNodeException) {
        } catch (e: IllegalStateException) {
        }

    throw NoNodeException()
}