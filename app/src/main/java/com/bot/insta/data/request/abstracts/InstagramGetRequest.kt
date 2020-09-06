package com.bot.insta.data.request.abstracts

import com.bot.insta.data.variables.AppValues.API_URL
import com.bot.insta.data.variables.AppValues.USER_AGENT
import okhttp3.Request

abstract class InstagramGetRequest<T> : InstagramRequest<T>() {

    override val method: String
        get() = "GET"

    override fun execute(): T {
        val request = Request.Builder().apply {
            url(API_URL + url)
            addHeader("Connection", "close")
            addHeader("Accept", "*/*")
            addHeader("Cookie2", "\$Version=1")
            addHeader("Accept-Language", "en-US")
            addHeader("User-Agent", USER_AGENT)
        }.build()

        val response = api?.client?.newCall(request)?.execute()
        api?.lastResponse = response

        val resultCode = response?.code()?:0
        val content =  response?.body()?.string()?:""

        return parseResult(resultCode,content)
    }

}