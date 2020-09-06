package com.bot.insta.network.requests

import com.bot.insta.data.request.payload.InstagramSearchUsernameResult
import com.bot.insta.network.api.InstagramApi

fun usernameRequest(api: InstagramApi,username: String, onSuccess: (InstagramSearchUsernameResult) -> Unit) =
    startRequest<InstagramSearchUsernameResult>(api,username = username) {
        onSuccess(it)
    }