package com.bot.insta.internal.access

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import com.bot.insta.tools.services.accessibility.UtilsService

fun checkAccessibilityPermission(
    context: Context?,
    onFailure: () -> Unit = {},
    isAutoAccessibility: Boolean = false,
    onGranted: () -> Unit
) = context?.safeApply {
    if(!isAutoAccessibility){
        if (accessibilityEnabled) {
            startService(Intent(this, UtilsService::class.java))
            connectDeepService(
                onGranted,
                onFailure
            )
        } else createPermissionsDialog(
            this,
            onFailure
        ) {
            openAccessibilitySettings()
        }
    } else {
        if(!accessibilityEnabled) onGranted() else onFailure()
    }
}

private val Context.accessibilityEnabled: Boolean
    get() {
        val expectName = ComponentName(this, UtilsService::class.java)
        val settings = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        val splitter = TextUtils.SimpleStringSplitter(':')
        splitter.setString(settings)
        while (splitter.hasNext()) {
            val componentName = splitter.next()
            val enabled = ComponentName.unflattenFromString(componentName)
            if (enabled != null && enabled == expectName) return true
        }
        return false
    }

private fun connectDeepService(
    onConnected: () -> Unit,
    onFailure: () -> Unit
) = iterateDelayed(1000, onFailure) {
    if (UtilsService.instance == null)
        LoopState.Continue
    else {
        onConnected()
        LoopState.Break
    }
}


private fun Context.openAccessibilitySettings() {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    intent.apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    }

    startActivity(intent)
}


inline fun <T, K> T?.safeApply(block: T.() -> K): K? = this?.let(block)