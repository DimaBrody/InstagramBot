package com.bot.insta.internal

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.bot.insta.internal.access.*
import com.bot.insta.internal.conditions.ButtonCondition
import com.bot.insta.tools.services.accessibility.UtilsService

private const val DELAY_AFTER_CLICK = 0L

 const val DEFAULT_TIMEOUT = 5000L


private fun AccessibilityNodeInfo.findNode(
    context: Context,
    condition: Int? = null,
    onSetup: (ButtonCondition.(Int) -> Boolean)? = null
): AccessibilityNodeInfo {

    if (condition == null) {

    } else {
        val checker = ButtonCondition(context, this)
        when {
            onSetup != null -> {
                if (onSetup(checker, condition)) return this
            }
            else -> {
                if (checker(condition)) return this
            }
        }
    }

    for (i in 0 until childCount)
        try {
            return getChild(i).findNode(context,condition,onSetup)
        } catch (e: NoNodeException) {
        } catch (e: IllegalStateException) {
        }

    throw NoNodeException()
}

fun searchAndClick(
    context: Context,
    condition: Int? = null,
    onSetup: (ButtonCondition.(Int) -> Boolean)? = null,
    onTimeOut: () -> Unit = { log("FAILED") },
    timeout: Long = DEFAULT_TIMEOUT,
    onComplete: () -> Unit
) {
    iterateDelayed(timeout, onTimeOut) {
        try {
            val neededNode = findNode(context,condition,onSetup)
            // log("-------------------------------------------------")

            post(DELAY_AFTER_CLICK) {
                neededNode.click()
                iterateCurrentScreen(
                    onComplete, DELAY_AFTER_CLICK,
                    onSuccess = onComplete
                )
            }
            //log("-------------------------------------------------")
            LoopState.Break
        } catch (e: NoNodeException) {
            Log.d(INNER_TAG, "Node Not Found")
            LoopState.Continue
        } catch (e: Exception) {
            e.printStackTrace()
            LoopState.Continue
        }
    }
}


 fun AccessibilityNodeInfo.click() {
    performAction(AccessibilityNodeInfo.ACTION_CLICK)
    if (parent != null) parent.click()
//     for(i in 0 until childCount)
//         getChild(i).click()
}

@Throws(NoNodeException::class)
fun findNode(
    context: Context,
    condition: Int? = null,
    onSetup: (ButtonCondition.(Int) -> Boolean)? = null
): AccessibilityNodeInfo {
    val window = UtilsService.instance!!.rootInActiveWindow
    return window.findNode(context, condition, onSetup)
}

class NoNodeException : Exception()

fun checkOverlayPermission(
    context: Context?,
    onFailure: () -> Unit = {},
    onGranted: () -> Unit
) = context?.safeApply {
    if (overlayEnabled)
        onGranted()
    else createPermissionsDialog(
        this,
        onFailure
    ) {
        openOverlay()
    }
}


val Context.overlayEnabled: Boolean
    get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        true else Settings.canDrawOverlays(this)

@TargetApi(Build.VERSION_CODES.M)
private fun Context.openOverlay() {
    val intent = Intent(
        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        Uri.parse("package:${packageName}")
    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    if (intent.resolveActivity(packageManager) != null)
        startActivity(intent)
}


fun iterateCurrentScreen(
    onFailed: () -> Unit = {},
    delay: Long = 500,
    timeout: Long = 1500,
    onSuccess: () -> Unit
) = post(delay) {
    iterateConditionally(
        onComplete = onSuccess,
        screen = false,
        timeout = timeout,
        onTimeOut = onFailed
    )
}

const val PACKAGE_SETTINGS = "com.instagram.android"
private const val PREFIX = "package:"
 const val INNER_TAG = "InstagramWindow"

fun Any?.log(message: Any?) =
    Log.d(this?.javaClass?.simpleName ?: "null", message.toString())

fun launchSettingsScreen(context: Context) =
    context.startActivity(createSettingsIntent(context))

private fun createSettingsIntent(context: Context): Intent {
    val intent = context.packageManager.getLaunchIntentForPackage(PACKAGE_SETTINGS)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        intent?.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
    } else {
        intent?.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
    }
    return intent!!
}