package com.bot.insta.data.request.payload

import com.bot.insta.data.model.InstagramUserSummary
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class InstagramGetUserFollowersResult(
    var status: String? = null,
    var message: String? = null,
    var big_list: Boolean = false,
    var next_max_id: String? = null,
    var page_size: Int = 0,
    var users: List<InstagramUserSummary>? = null
)