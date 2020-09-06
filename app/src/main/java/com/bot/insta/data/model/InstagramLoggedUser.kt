package com.bot.insta.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class InstagramLoggedUser(
    val profile_pic_url: String,
    val allow_contact_sync : Boolean,
    val username: String,
    val full_name: String,
    val is_private: Boolean,
    val profile_pic_id: String,
    val pk: Long,
    val is_verified: String,
    val has_anonymous_profile_picture: Boolean
)