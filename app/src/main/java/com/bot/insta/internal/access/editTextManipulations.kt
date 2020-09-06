package com.bot.insta.internal.access

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.bot.insta.App
import com.bot.insta.data.variables.AppValues
import com.bot.insta.internal.*
import com.bot.insta.internal.conditions.ButtonCondition

private const val DELAY_AFTER_CLICK = 250L

fun searchAndPrint(
    context: Context,
    condition: Int? = null,
    type: InputTextType = InputTextType.ACCOUNTS,
    text: String? = null,
    onSetup: (ButtonCondition.(Int) -> Boolean)? = null,
    onTimeOut: () -> Unit = { log("FAILED") },
    timeout: Long = DEFAULT_TIMEOUT,
    onComplete: () -> Unit
){
    iterateDelayed(timeout, onTimeOut) {
        try {
            val neededNode = findNode(context,condition,onSetup)

            var resultText = text

            try {
                if(resultText == null)
                    resultText = when(type){
                        InputTextType.ACCOUNTS->
                            App.listOfAccounts.random()
                    }
            } catch (e: NoSuchElementException){
                resultText = "not found"
            }

            AppValues.currentAccount = resultText?:""

            post(DELAY_AFTER_CLICK) {
                neededNode.putText(resultText)
                iterateCurrentScreen(
                    onComplete, DELAY_AFTER_CLICK,
                    onSuccess = onComplete
                )
            }

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


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
private fun AccessibilityNodeInfo?.putText(text: String?) {
    if (this != null && className == "android.widget.EditText") {
        performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, Bundle().apply {
            putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text)
        })
    }
}

enum class InputTextType {
    ACCOUNTS
}