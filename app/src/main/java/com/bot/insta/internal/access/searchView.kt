package com.bot.insta.internal.access

import android.content.Context
import android.util.Log
import com.bot.insta.internal.*
import com.bot.insta.internal.conditions.ButtonCondition

private const val DELAY_AFTER_CLICK = 0L

fun searchView(
    context: Context,
    condition: Int? = null,
    onSetup: (ButtonCondition.(Int) -> Boolean)? = null,
    onTimeOut: () -> Unit = { log("FAILED") },
    timeout: Long = DEFAULT_TIMEOUT,
    onFollowers: () -> Unit,
    onFollowing: () -> Unit
) {
    lateinit var neededFunction: () -> Unit

    iterateDelayed(timeout, onTimeOut) {
        try {
            val neededNode = findNode(context, condition, onSetup)
            // log("-------------------------------------------------")

            post(DELAY_AFTER_CLICK) {
                neededFunction = when (neededNode.text.toString()) {
                    "brodywear" -> onFollowing
                    else -> onFollowers
                }

                iterateCurrentScreen(
                    neededFunction, DELAY_AFTER_CLICK,
                    onSuccess = neededFunction
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