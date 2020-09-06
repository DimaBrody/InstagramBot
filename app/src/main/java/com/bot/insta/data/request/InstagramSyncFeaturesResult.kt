package com.bot.insta.data.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class InstagramSyncFeaturesResult(
    val status: String? = null,
    val message: String? = null
)