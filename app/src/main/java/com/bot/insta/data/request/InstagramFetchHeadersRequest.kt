package com.bot.insta.data.request

import com.bot.insta.data.request.abstracts.InstagramGetRequest
import com.bot.insta.data.request.payload.StatusResult
import com.bot.insta.tools.uuid.generateUuid

class InstagramFetchHeadersRequest : InstagramGetRequest<StatusResult>() {

    override val url: String
        get() = "si/fetch_headers/?challenge_type=signup&guid=" + generateUuid(false)

    override fun requiresLogin(): Boolean = false

    override fun parseResult(resultCode: Int, content: String): StatusResult {
        return parseJson(resultCode,content,StatusResult::class.java)
    }

}