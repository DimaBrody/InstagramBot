package com.bot.insta.network.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bot.insta.data.request.payload.InstagramGetUserFollowersResult
import com.bot.insta.data.request.payload.InstagramLoginResult
import com.bot.insta.data.request.payload.InstagramSearchUsernameResult
import com.bot.insta.data.request.payload.StatusResult
import com.bot.insta.network.api.InstagramApi
import com.bot.insta.network.requests.startRequest
import com.bot.insta.network.threads.post

class NetworkDataSourceImpl(
    private val api: InstagramApi
) : NetworkDataSource {

    //FOLLOWING USER

    override fun followingRequest(
        userPk: Long,
        maxID: String?,
        onFailed: (Throwable?) -> Unit,
        onSuccess: (InstagramGetUserFollowersResult) -> Unit
    ) = startRequest(
        api,onFailed,
        userPk = userPk,
        maxID = maxID,
        onSuccess = onSuccess,
        isFollowing = true
    )

    //FOLLOWERS USER

    override fun followersRequest(
        userPk: Long,
        maxID: String?,
        onFailed: (Throwable?) -> Unit,
        onSuccess: (InstagramGetUserFollowersResult) -> Unit
    ) = startRequest(
        api, onFailed,
        userPk = userPk,
        maxID = maxID,
        onSuccess = onSuccess
    )


    //LOGIN ACC

    private val _instagramLoginData = MutableLiveData<InstagramLoginResult>()

    override val loginData: LiveData<InstagramLoginResult>
        get() = _instagramLoginData

    override fun loginRequest(isFacebook: Boolean, onFailed: (Throwable?) -> Unit) {
        startRequest<InstagramLoginResult>(
            api, onFailed,isFacebook = isFacebook
        ) {
            post { _instagramLoginData.value = it }
        }
    }

    //UNFOLLOW REQUEST

    override fun unfollowRequest(
        userPk: Long,
        onFailed: (Throwable?) -> Unit,
        onSuccess: (StatusResult) -> Unit
    ) = startRequest(
        api,onFailed,userPk = userPk,
        onSuccess = onSuccess,
        isUnfollow = true
    )

    //FOLLOW USER

    override fun followRequest(
        userPk: Long,
        onFailed: (Throwable?) -> Unit,
        onSuccess: (StatusResult) -> Unit
    ) = startRequest(
        api, onFailed, userPk = userPk,
        onSuccess = onSuccess
    )

    //USERNAME ACC DATA

    override fun userRequest(
        username: String,
        onFailed: (Throwable?) -> Unit,
        onSuccess: (InstagramSearchUsernameResult) -> Unit
    ) = startRequest<InstagramSearchUsernameResult>(
            api, onFailed, username
        ) {
        onSuccess(it)
    }


}

