package com.bot.insta.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class InstagramProfilePic(
    var url: String? = null,
    var width : Int = 0,
    var height : Int = 0
)
