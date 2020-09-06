package com.bot.insta.internal

import android.util.Log
import java.lang.Exception

private const val TAG = "Insta"

fun Any?.log(message: Any?, isPrint: Boolean = true) =
    if (isPrint) Log.d(this?.javaClass?.simpleName ?: "null", message.toString())
    else 0

fun log(message: Any?) =
    Log.d(TAG, message.toString())

fun log(tag: Any?, message: Any?, throwable: Throwable) =
    Log.d(tag.toString(), message.toString(), throwable)

fun simpleTry(onTry: () -> Unit){
    try {
        onTry()
    } catch (e: Exception){
        e.printStackTrace()
    }
}

inline fun <T, K> T?.safeApply(block: T.() -> K): K? = this?.let(block)