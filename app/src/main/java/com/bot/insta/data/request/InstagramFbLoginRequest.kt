package com.bot.insta.data.request

import com.bot.insta.data.request.abstracts.InstagramPostRequest
import com.bot.insta.data.request.payload.InstagramFbLoginPayload
import com.bot.insta.data.request.payload.InstagramLoginResult
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.lang.Exception

class InstagramFbLoginRequest(
    private val fbPayload: InstagramFbLoginPayload
) : InstagramPostRequest<InstagramLoginResult>() {

    override val url: String
        get() = "fb/facebook_signup/"

    override val payload: String?
        get(){
            try {
                val mapper = ObjectMapper().registerKotlinModule()
                return mapper.writeValueAsString(fbPayload)
            } catch (e: Exception){
                throw e
            }
        }

    override fun parseResult(resultCode: Int, content: String): InstagramLoginResult {
        try {
            return parseJson(resultCode,content,InstagramLoginResult::class.java)
        } catch (e: Exception){
            throw e
        }
    }

    override fun requiresLogin(): Boolean = false

}