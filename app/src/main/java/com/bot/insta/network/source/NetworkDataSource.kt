package com.bot.insta.network.source

import androidx.lifecycle.LiveData
import com.bot.insta.data.request.payload.InstagramGetUserFollowersResult
import com.bot.insta.data.request.payload.InstagramLoginResult
import com.bot.insta.data.request.payload.InstagramSearchUsernameResult
import com.bot.insta.data.request.payload.StatusResult

interface NetworkDataSource {

    val loginData: LiveData<InstagramLoginResult>

    fun userRequest(
        username: String,
        onFailed: (Throwable?) -> Unit = defaultFailed,
        onSuccess: (InstagramSearchUsernameResult) -> Unit
    )

    fun loginRequest(
        isFacebook: Boolean = false,
        onFailed: (Throwable?) -> Unit = defaultFailed
    )

    fun followRequest(
        userPk: Long,
        onFailed: (Throwable?) -> Unit = defaultFailed,
        onSuccess: (StatusResult) -> Unit
    )

    fun unfollowRequest(
        userPk: Long,
        onFailed: (Throwable?) -> Unit = defaultFailed,
        onSuccess: (StatusResult) -> Unit
    )

    fun followingRequest(
        userPk: Long,
        maxID: String? = null,
        onFailed: (Throwable?) -> Unit = defaultFailed,
        onSuccess: (InstagramGetUserFollowersResult) -> Unit
    )

    fun followersRequest(
        userPk: Long,
        maxID: String? = null,
        onFailed: (Throwable?) -> Unit = defaultFailed,
        onSuccess: (InstagramGetUserFollowersResult) -> Unit
    )



    companion object {
        private val defaultFailed: (Throwable?) -> Unit
            get() = { it?.printStackTrace() }
    }

}
