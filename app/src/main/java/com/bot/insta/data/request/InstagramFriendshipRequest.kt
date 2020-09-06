package com.bot.insta.data.request

import com.bot.insta.data.request.abstracts.InstagramPostRequest
import com.bot.insta.data.request.payload.StatusResult
import com.fasterxml.jackson.databind.ObjectMapper

class InstagramFriendshipRequest(
    private val userID: Long,
    private val isUnfollow: Boolean = false
) : InstagramPostRequest<StatusResult>() {

    override val url: String
        get() = "friendships/${if(!isUnfollow) "create" else "destroy"}/$userID/"

    override val payload: String?
        get(){
            val likeMap = LinkedHashMap<String,Any>()
            likeMap["_uuid"] = api?.uuid?:""
            likeMap["_uid"] = api?.userID?:""
            likeMap["user_id"] = userID
            likeMap["_csrftoken"] = api?.getOrFetchCsrf(null)?:""

            return ObjectMapper().writeValueAsString(likeMap)
        }

    override fun parseResult(resultCode: Int, content: String): StatusResult {
        return parseJson(resultCode,content,StatusResult::class.java)
    }
}