package com.bot.insta.tools.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.bot.insta.components.dependencies.instance
import com.bot.insta.components.dependencies.prefs
import com.bot.insta.data.model.InstagramUserSummary
import com.bot.insta.data.request.payload.InstagramGetUserFollowersResult
import com.bot.insta.internal.iterate
import com.bot.insta.network.source.NetworkDataSource
import com.bot.insta.tools.notifications.NotificationProvider

class UnFollowingService: Service() {

    private val dataSource: NetworkDataSource by instance()

    private val currentUsers = mutableListOf<InstagramUserSummary>()

    private val userPk = prefs.userPk

    private var nextMaxID : String? = null

    private var currentCount: Int = 0
        set(value) {
            NotificationProvider.updateNotification(field)
            field = value
        }


    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        setupService()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun setupService(){
        fetchFollowing {
            NotificationProvider.createNotification(currentUsers.size)
            currentUsers.subList(0,50).iterate(
                iteration = { user,next ->
                    unFollowUser(user,next)
                },
                complete = {
                    destroy()
                }
            )
        }
    }


    private fun fetchFollowing(
        onFailed: (Throwable?) -> Unit = {},
        onSuccess: (InstagramGetUserFollowersResult) -> Unit
    ) {
        dataSource.followingRequest(userPk, nextMaxID, onFailed) {
            if (it.status == "ok") {
                nextMaxID = it.next_max_id
                currentUsers.addAll(it.users?: mutableListOf())
                if(nextMaxID != null){
                    fetchFollowing(onFailed,onSuccess)
                } else onSuccess(it)
            } else onFailed(null)
        }
    }

    private fun unFollowUser(user: InstagramUserSummary, onNext: () -> Unit) {
        dataSource.unfollowRequest(user.pk, { onNext() }) {
            if (it.status == "ok")
                currentCount++
            onNext()
        }
    }

    private fun destroy() {
        NotificationProvider.cancelNotification()
        stopSelf()
    }


}