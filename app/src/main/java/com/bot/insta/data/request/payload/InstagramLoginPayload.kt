package com.bot.insta.data.request.payload

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class InstagramLoginPayload(
    var username: String? = null,
    var phone_id: String? = null,
    var _csrftoken: String? = null,
    var guid: String? = null,
    var device_id: String? = null,
    var password: String? = null,
    var login_attempt_account : Int = 0
)