package com.bot.insta.data.request

import com.bot.insta.data.request.abstracts.InstagramGetRequest
import com.bot.insta.data.request.payload.InstagramGetUserFollowersResult

class InstagramGetUserFollowersRequest(
    private val userID: Long,
    private val maxID: String? = null,
    private val isFollowing: Boolean = false
) : InstagramGetRequest<InstagramGetUserFollowersResult>() {

    override val url: String
        get() {
            var baseUrl =
                "friendships/$userID/${if (!isFollowing) "followers" else "following"}/?rank_token=${api?.rankToken}&ig_sig_key_version=" + "4"
            if (maxID != null && maxID.isNotEmpty())
                baseUrl += "&max_id=$maxID"

            return baseUrl
        }

    override fun parseResult(resultCode: Int, content: String): InstagramGetUserFollowersResult {
        return parseJson(resultCode, content, InstagramGetUserFollowersResult::class.java)
    }


}