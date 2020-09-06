package com.bot.insta.data.request

import com.bot.insta.data.request.abstracts.InstagramPostRequest
import com.bot.insta.data.request.payload.InstagramSyncFeaturesPayload
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.lang.Exception
import java.lang.NullPointerException

class InstagramSyncFeaturesRequest(mPayload: InstagramSyncFeaturesPayload?) :
    InstagramPostRequest<InstagramSyncFeaturesResult>() {

    override val url: String
        get() = "qe/sync/"

    private lateinit var mPayload : InstagramSyncFeaturesPayload

    override val payload: String?
        get() = try {
            val mapper = ObjectMapper().registerKotlinModule()
            mapper.writeValueAsString(mPayload)
        } catch (e: Exception){
            throw e
        }

    override fun parseResult(resultCode: Int, content: String): InstagramSyncFeaturesResult {
        try {
            return InstagramSyncFeaturesResult()
        } catch (e: Exception){
            throw e
        }
    }

    init {
        if(mPayload == null)
            throw NullPointerException("Payload")
        else this.mPayload = mPayload
    }
}