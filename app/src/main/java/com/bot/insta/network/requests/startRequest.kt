package com.bot.insta.network.requests

import com.bot.insta.components.dependencies.prefs
import com.bot.insta.data.request.InstagramFriendshipRequest
import com.bot.insta.data.request.InstagramGetUserFollowersRequest
import com.bot.insta.data.request.InstagramSearchUsernameRequest
import com.bot.insta.data.request.payload.InstagramGetUserFollowersResult
import com.bot.insta.data.request.payload.InstagramLoginResult
import com.bot.insta.data.request.payload.InstagramSearchUsernameResult
import com.bot.insta.data.request.payload.StatusResult
import com.bot.insta.network.api.InstagramApi
import com.bot.insta.network.threads.networkRequest
import java.lang.IllegalArgumentException


inline fun <reified R> startRequest(
    instagramApi: InstagramApi,
    noinline onFailed: (Throwable?) -> Unit = {},
    username: String = "",
    userPk: Long = 0L,
    maxID: String? = null,
    isFacebook: Boolean = false,
    isFollowing: Boolean = false,
    isUnfollow: Boolean = false,
    crossinline onSuccess: (R) -> Unit = {}
) = networkRequest(onFailed) {
    with(R::class.java) {
        when {
            isAssignableFrom(InstagramLoginResult::class.java) -> {
                instagramApi.setup()
                onSuccess(
                    if (!isFacebook) {
                        instagramApi.login(
                            prefs.username,
                            prefs.password
                        ) as R
                    } else instagramApi.loginFb(
                        prefs.username,
                        prefs.password
                    ) as R
                )
            }
            isAssignableFrom(InstagramSearchUsernameResult::class.java) -> {
                onSuccess(
                    instagramApi.sendRequest(
                        InstagramSearchUsernameRequest(username)
                    ) as R
                )
            }
            isAssignableFrom(InstagramGetUserFollowersResult::class.java) -> {
                onSuccess(
                    instagramApi.sendRequest(
                        InstagramGetUserFollowersRequest(userPk, maxID,isFollowing)
                    ) as R
                )
            }
            isAssignableFrom(StatusResult::class.java) -> {
                onSuccess(
                    instagramApi.sendRequest(
                        InstagramFriendshipRequest(userPk,isUnfollow)
                    ) as R
                )
            }
            else -> throw IllegalArgumentException("Request not found")
        }
    }
}