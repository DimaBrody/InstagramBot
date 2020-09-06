package com.bot.insta.data.request

import com.bot.insta.data.request.abstracts.InstagramPostRequest
import com.bot.insta.data.request.payload.InstagramLoginPayload
import com.bot.insta.data.request.payload.InstagramLoginResult
import com.fasterxml.jackson.databind.ObjectMapper

class InstagramLoginRequest(
    private val mPayload: InstagramLoginPayload
) : InstagramPostRequest<InstagramLoginResult>() {

    override val url: String
        get() = "accounts/login/"

    override val payload: String?
        get() {
            val mapper = ObjectMapper()
            return mapper.writeValueAsString(mPayload)
        }

    override fun parseResult(resultCode: Int, content: String): InstagramLoginResult {
        return parseJson(resultCode,content,InstagramLoginResult::class.java)
    }

    override fun requiresLogin(): Boolean = false

}