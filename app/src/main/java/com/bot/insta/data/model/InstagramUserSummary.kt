package com.bot.insta.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class InstagramUserSummary(
    var is_verified: Boolean = false,
    var profile_pic_id: String? = null,
    var is_favorite: Boolean = false,
    var is_private: Boolean = false,
    var username: String? = null,
    var pk: Long = 0,
    var profile_pic_url: String? = null,
    var has_anonymous_profile_picture: Boolean = false,
    var full_name: String? = null
){
    override fun equals(other: Any?): Boolean {
        return pk == (other as? InstagramUserSummary)?.pk
    }

    override fun hashCode(): Int {
        var result = is_verified.hashCode()
        result = 31 * result + (profile_pic_id?.hashCode() ?: 0)
        result = 31 * result + is_favorite.hashCode()
        result = 31 * result + is_private.hashCode()
        result = 31 * result + (username?.hashCode() ?: 0)
        result = 31 * result + pk.hashCode()
        result = 31 * result + (profile_pic_url?.hashCode() ?: 0)
        result = 31 * result + has_anonymous_profile_picture.hashCode()
        result = 31 * result + (full_name?.hashCode() ?: 0)
        return result
    }
}