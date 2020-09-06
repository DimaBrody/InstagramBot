package com.bot.insta.data.request.abstracts

import android.util.Log
import com.bot.insta.data.variables.AppValues
import com.bot.insta.data.variables.AppValues.APP_ID
import com.bot.insta.data.variables.AppValues.USER_AGENT
import com.bot.insta.tools.hash.generateSignature
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody

abstract class InstagramPostRequest<T> : InstagramRequest<T>() {

    override val method: String
        get() = "POST"

    override fun execute(): T {

        val request = Request.Builder().apply {
            url(AppValues.API_URL + url)
            addHeader("Connection", "close")
            addHeader("Accept", "*/*")
            addHeader("Cookie2", "\$Version=1")
            addHeader("Accept-Language", "en-US")
            addHeader("X-IG-Capabilities", "3boBAA==")
            addHeader("X-IG-Connection-Type", "WIFI")
            addHeader("X-IG-Connection-Speed", "-1kbps")
            addHeader("X-IG-App-ID", APP_ID)
            addHeader("User-Agent", USER_AGENT)
            post(
                RequestBody.create(
                    MediaType.parse("application/x-www-form-urlencoded"),
                    generateSignature(payload)
                )
            )
        }.build()

        val response = api?.client?.newCall(request)?.execute()
        api?.lastResponse = response

        val resultCode = response?.code() ?: 0
        val content = response?.body()?.string() ?: ""

        Log.d("AAA",content)

        return parseResult(resultCode, content)
    }

}