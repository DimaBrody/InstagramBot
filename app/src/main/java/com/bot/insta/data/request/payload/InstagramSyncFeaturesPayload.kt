package com.bot.insta.data.request.payload

data class InstagramSyncFeaturesPayload(
    val _uuid: String = "",
    val _uid: Long = 0L,
    val id: Long = 0L,
    val _csrftoken: String = "",
    val experiments: String = ""
)