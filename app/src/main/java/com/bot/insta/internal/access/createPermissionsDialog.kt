package com.bot.insta.internal.access

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.bot.insta.App
import com.bot.insta.R


fun createPermissionsDialog(
    context: Context,
    onFailure: () -> Unit,
    onGranted: () -> Unit
) =
    AlertDialog.Builder(
        if (context is Activity) context else
            App.activityLifecycleCallback.currentActivity ?: context
    ).apply {

        setTitle("Hello")

        setMessage("World")
        setNegativeButton("Cancel") { dialog, _ ->
            onFailure()
            dialog.dismiss()
        }

        setPositiveButton("OK") { dialogInterface, _ ->
            onGranted()
            dialogInterface.dismiss()
        }

        show()
    }