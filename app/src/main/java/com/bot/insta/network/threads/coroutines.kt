package com.bot.insta.network.threads

import com.bot.insta.App
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

private lateinit var innerOnFailed: (Throwable?) -> Unit

private val coroutineIOJob: Job
    get() = Job()

private val coroutineIOContext: CoroutineContext
    get() = coroutineIOJob + Dispatchers.IO

private val coroutineIOScope: CoroutineScope
    get() = CoroutineScope(coroutineIOContext)

private val coroutineIOExceptionHandler: CoroutineExceptionHandler
    get() = CoroutineExceptionHandler { _, t ->
        innerOnFailed(t)
        t.printStackTrace()
    }
private val coroutineMainScope: CoroutineScope
    get() = MainScope()

private val coroutineMainExceptionHandler: CoroutineExceptionHandler
    get() = CoroutineExceptionHandler { _, t ->
        t.printStackTrace()
    }

fun mainThread(onMain: () -> Unit){
    coroutineMainScope.launch(coroutineMainExceptionHandler) {
        onMain()
    }
}

fun post(onMainPost: () -> Unit) = App.handler!!.post(onMainPost)

fun networkRequest(onFailed: (Throwable?) -> Unit = {}, onRequest: CoroutineScope.() -> Unit) {
    innerOnFailed = onFailed
    coroutineIOScope.launch(coroutineIOExceptionHandler) {
        onRequest()
    }
}