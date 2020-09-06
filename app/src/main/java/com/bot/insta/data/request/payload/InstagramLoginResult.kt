package com.bot.insta.data.request.payload

import com.bot.insta.data.model.InstagramLoggedUser
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class InstagramLoginResult(
    var status: String = "",
    var message: String = "",
    var logged_in_user: InstagramLoggedUser? = null,
    var error_type: String = "",
    var checkpoint_url: String = ""
)