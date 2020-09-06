package com.bot.insta.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class InstagramUser(
    var is_private: Boolean = false,
    var is_verified: Boolean = false,
    var username: String? = null,
    var has_chaining: Boolean = false,
    var is_business: Boolean = false,
    var media_count: Int = 0,
    var profile_pic_id: String? = null,
    var external_url: String? = null,
    var full_name: String? = null,
    var has_biography_translation: Boolean = false,
    var has_anonymous_profile_picture: Boolean = false,
    var is_favorite: Boolean = false,
    var public_phone_country_code: String? = null,
    var public_phone_number: String? = null,
    var public_email: String? = null,
    var pk: Long = 0,
    var geo_media_count: Int = 0,
    var usertags_count: Int = 0,
    var profile_pic_url: String? = null,
    var address_street: String? = null,
    var city_name: String? = null,
    var zip: String? = null,
    var direct_messaging: String? = null,
    var business_contact_method: String? = null,
    var biography: String? = null,
    var follower_count: Int = 0,
    var hd_profile_pic_versions: List<InstagramProfilePic?>? = null,
    var hd_profile_pic_url_info: InstagramProfilePic? = null,
    var external_lynx_url: String? = null,
    var following_count: Int = 0,
    var latitude: Float = 0f,
    var longitude: Float = 0f,
    var status: String? = null
)