package com.bot.insta.data.request

import com.bot.insta.data.request.abstracts.InstagramGetRequest
import com.bot.insta.data.request.payload.InstagramSearchUsernameResult
import java.lang.Exception
import java.text.ParseException

class InstagramSearchUsernameRequest(
    private val username: String
) : InstagramGetRequest<InstagramSearchUsernameResult>() {

    override val url: String
        get() = "users/$username/usernameinfo/"

    override fun parseResult(resultCode: Int, content: String): InstagramSearchUsernameResult {
        try {
//            log(content)
            return parseJson(resultCode,content,InstagramSearchUsernameResult::class.java)
        } catch (e: Exception){
            throw ParseException("",0)
        }
    }

}