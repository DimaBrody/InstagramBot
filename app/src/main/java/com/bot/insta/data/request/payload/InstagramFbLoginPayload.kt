package com.bot.insta.data.request.payload

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class InstagramFbLoginPayload(
    var dryrun: Boolean = false,
    var phone_id: String? = null,
    var adid: String? = null,
    var device_id: String? = null,
    var waterfall_id: String? = null,
    var fb_access_token: String? = null
)