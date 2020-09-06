package com.bot.insta.data.request.payload

import com.bot.insta.data.model.InstagramUser
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class InstagramSearchUsernameResult(
    var user: InstagramUser? = null,
    var status: String? = null,
    var message: String? = null
)