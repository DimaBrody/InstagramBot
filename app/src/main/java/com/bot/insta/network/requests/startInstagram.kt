package com.bot.insta.network.requests

import com.bot.insta.components.dependencies.instagramApi
import com.bot.insta.network.threads.networkRequest

fun startInstagram(onFailed: () -> Unit,onSuccess: ()-> Unit) = networkRequest {
    instagramApi.setup()
    if(instagramApi.login().status == "ok")
        onSuccess() else onFailed()
}