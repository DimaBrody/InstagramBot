package com.bot.insta.data.request.abstracts

import com.bot.insta.data.request.payload.StatusResult
import com.bot.insta.network.api.InstagramApi
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.IOException

@Suppress("UNCHECKED_CAST")
abstract class InstagramRequest<T> {

    var api: InstagramApi? = null

    open val payload: String? = null

    abstract val method: String

    abstract val url: String

    @Throws(IOException::class)
    abstract fun execute(): T

    abstract fun parseResult(resultCode: Int, content: String): T

    open fun requiresLogin(): Boolean {
        return true
    }

    open fun <U> parseJson(statusCode: Int, str: String?, clazz: Class<U>): U {
        if (clazz.isAssignableFrom(StatusResult::class.java)) { //TODO: implement a better way to handle exceptions
            if (statusCode == 404) {
                val result: StatusResult = clazz.newInstance() as StatusResult
                result.status = "error"
                result.message = "SC_NOT_FOUND"
                return result as U
            } else if (statusCode == 403) {
                val result: StatusResult = clazz.newInstance() as StatusResult
                result.status = "error"
                result.message = "SC_FORBIDDEN"
                return result as U
            }
        }
        return parseJson(str, clazz)
    }

    private fun <U> parseJson(
        str: String?,
        clazz: Class<U>?
    ): U {
        val objectMapper =
            ObjectMapper().configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH, false)
                .registerKotlinModule()
        return objectMapper.readValue(str, clazz)
    }

}