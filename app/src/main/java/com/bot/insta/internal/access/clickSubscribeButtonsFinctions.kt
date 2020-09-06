package com.bot.insta.internal.access

import android.content.Context
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.bot.insta.components.dependencies.prefs
import com.bot.insta.internal.INNER_TAG
import com.bot.insta.internal.NoNodeException
import com.bot.insta.internal.conditions.ButtonCondition
import com.bot.insta.internal.instagram.root.CommandExecutor
import com.bot.insta.internal.log
import com.bot.insta.internal.model.SubscribersContainer
import com.bot.insta.tools.services.accessibility.UtilsService
import java.util.concurrent.TimeUnit

private const val MAX_FAILED_CLICKS = 1

private val CLICK_TIMEOUT = TimeUnit.MINUTES.toMillis(5)

private const val DELAY_AFTER_CLICK = 0L

private var notFoundCounter = 0

private val subscribersContainer = SubscribersContainer()

fun clickEverySubscribeButton(
    context: Context,
    condition: Int? = null,
    onSetup: (ButtonCondition.(Int) -> Boolean)? = null,
    onTimeOut: () -> Unit = { log("FAILED") },
    timeout: Long = CLICK_TIMEOUT,
    onComplete: () -> Unit
) {
    iterateDelayed(timeout, onTimeOut) {
        try {
            val node = findAllSubscribersNodes(context, condition, onSetup)
            node.defaultClick()
//            val scrollableNode = scrollNode(context, SCROLLABLE_VIEW)
//            scrollableNode.scrollListView()
            checkEverything(false,onComplete)
        } catch (e: NoNodeException) {
            Log.d(INNER_TAG, "Node Not Found")
            checkEverything(true,onComplete)
        } catch (e: Exception) {
            e.printStackTrace()
            checkEverything(true,onComplete)
        }
    }
}


fun checkEverything(isSwipeNeeded: Boolean = false,onComplete: () -> Unit) : LoopState {

    if(isSwipeNeeded)
        CommandExecutor.instance.executeSwipe()

    log(subscribersContainer.currentCount)
    return if (subscribersContainer.currentCount >= prefs.peopleCount) {
        subscribersContainer.currentCount = 0
        onComplete()
        LoopState.Break
    } else LoopState.Continue
}

fun AccessibilityNodeInfo.defaultClick(){
    performAction(AccessibilityNodeInfo.ACTION_CLICK)
}

fun clickEveryUnFollowButton(
    context: Context,
    condition: Int? = null,
    onSetup: (ButtonCondition.(Int) -> Boolean)? = null,
    onTimeOut: () -> Unit = { log("FAILED") },
    timeout: Long = CLICK_TIMEOUT,
    onComplete: () -> Unit
) {
    iterateDelayed(timeout, onTimeOut) {
        try {
            val node = findAllSubscribersNodes(context, condition, onSetup)
            node.defaultClick()
//            val scrollableNode = scrollNode(context, SCROLLABLE_VIEW)
//            scrollableNode.scrollListView()
            notFoundCounter = 0
            checkEverythingForUnFollowers(false,onComplete)
        } catch (e: NoNodeException) {
            Log.d(INNER_TAG, "Node Not Found")
            notFoundCounter++
            checkEverythingForUnFollowers(true,onComplete)
        } catch (e: Exception) {
            e.printStackTrace()
            notFoundCounter++
            checkEverythingForUnFollowers(true,onComplete)
        }
    }
}

fun checkEverythingForUnFollowers(isSwipeNeeded: Boolean = false,onComplete: () -> Unit) : LoopState {

    if(isSwipeNeeded)
        CommandExecutor.instance.executeSwipe()

    log(notFoundCounter)
    return if (notFoundCounter >= MAX_FAILED_CLICKS) {
        notFoundCounter = 0
        onComplete()
        LoopState.Break
    } else LoopState.Continue
}


//
//private fun scrollNode(
//    context: Context,
//    condition: Int? = null,
//    onSetup: (ButtonCondition.(Int) -> Boolean)? = null
//): AccessibilityNodeInfo {
//    val window = UtilsService.instance!!.rootInActiveWindow
//    return window.scrollNode(context, condition)
//}

private fun findAllSubscribersNodes(
    context: Context,
    condition: Int? = null,
    onSetup: (ButtonCondition.(Int) -> Boolean)? = null
): AccessibilityNodeInfo {
    val window = UtilsService.instance!!.rootInActiveWindow
    return window.findAllSubscribersNodes(context, condition, onSetup)
}

private fun AccessibilityNodeInfo.findAllSubscribersNodes(
    context: Context,
    condition: Int? = null,
    onSetup: (ButtonCondition.(Int) -> Boolean)? = null
): AccessibilityNodeInfo {

    val checker = ButtonCondition(context, this)
    if (condition != null) {
        if (checker(condition)) {
            subscribersContainer.currentCount++
            return this
        } else {
            for (i in 0 until childCount)
                try {
                    return getChild(i).findAllSubscribersNodes(context, condition, onSetup)
                } catch (e: NoNodeException) {
                } catch (e: IllegalStateException) {
                }
        }
    }

    throw NoNodeException()
}

//private fun AccessibilityNodeInfo.scrollNode(
//    context: Context,
//    condition: Int? = null
//): AccessibilityNodeInfo {
//
//    if(condition != null){
//        val checker = ButtonCondition(context,this)
//        if(true){
//            scrollListView()
//        }
//    }
//
//    for (i in 0 until childCount)
//        try {
//            return getChild(i).scrollNode(context)
//        } catch (e: NoNodeException) {
//        } catch (e: IllegalStateException) {
//        }
//
//    throw NoNodeException()
//}

//private fun AccessibilityNodeInfo.scrollListView(){
//    performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)
//}