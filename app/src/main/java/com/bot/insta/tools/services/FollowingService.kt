package com.bot.insta.tools.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.bot.insta.App
import com.bot.insta.components.dependencies.instance
import com.bot.insta.components.dependencies.prefs
import com.bot.insta.data.model.InstagramUser
import com.bot.insta.data.model.InstagramUserSummary
import com.bot.insta.data.model.db.UserData
import com.bot.insta.data.repository.DatabaseRepository
import com.bot.insta.data.request.payload.InstagramGetUserFollowersResult
import com.bot.insta.internal.iterate
import com.bot.insta.internal.log
import com.bot.insta.network.source.NetworkDataSource
import com.bot.insta.tools.notifications.NotificationProvider
import java.util.concurrent.TimeUnit

class FollowingService : Service() {

    private val dataSource: NetworkDataSource by instance()

    private val databaseRepository: DatabaseRepository by instance()

    private val currentUsers = mutableListOf<InstagramUserSummary>()

    private val globalCount: Int = prefs.peopleCount

    private var nextMaxID: String? = null

    private var userPk: Long = 0L

    private var currentCount: Int = 0
        set(value) {
            NotificationProvider.updateNotification(field)
            field = value
        }

    private var currentUserCount: Int = 0

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        NotificationProvider.createNotification(globalCount)

        setupService()


        return super.onStartCommand(intent, flags, startId)
    }

    private fun setupService() {
        fetchUsername {
            observeUsers(it) {
                startFetching()
            }
        }
    }

    private fun startFetching() {
        fetchFollowers ({
            destroy()
        }){
            currentUsers.addAll(it.users ?: mutableListOf())
            if (currentUsers.size - currentUserCount - globalCount < 0) {
                if (nextMaxID == null) {
                    currentUsers.iterate(
                        iteration = iteration,
                        complete = complete
                    )
                } else startFetching()
            } else {
                currentUsers.subList(currentUserCount, currentUserCount + globalCount).iterate(
                    iteration = iteration,
                    complete = complete
                )
            }

        }
    }

    private val iteration: (InstagramUserSummary, () -> Unit) -> Unit
        get() = { user, next ->
            checkRequire {
                followUser(user, next)
            }
        }

    private val complete: () -> Unit
        get() = {
            checkRequire {
                startFetching()
            }
        }

    private fun checkRequire(onSuccess: () -> Unit) {
        if (currentCount < globalCount) {
            onSuccess()
        } else destroy()
    }

    private fun followUser(user: InstagramUserSummary, onNext: () -> Unit) {
        dataSource.followRequest(user.pk, { onNext() }) {
            if (it.status == "ok") {
                currentCount++
                onNext()
            } else if (it.status == "fail" && it.message.contains("few minutes")) {
                App.handler?.postDelayed({ onNext() }, TimeUnit.MINUTES.toMillis(1))
            } else onNext()
        }
    }

    private fun fetchFollowers(
        onFailed: (Throwable?) -> Unit = {},
        onSuccess: (InstagramGetUserFollowersResult) -> Unit
    ) {
        dataSource.followersRequest(userPk, nextMaxID, onFailed) {
            if (it.status == "ok") {
                nextMaxID = it.next_max_id
                onSuccess(it)
            } else onFailed(null)
        }
    }

    private fun fetchUsername(
        onFailed: (Throwable?) -> Unit = {},
        onSuccess: (InstagramUser) -> Unit
    ) {
        dataSource.userRequest(prefs.currentList.random().also { log(it) }, onFailed) {
            if (it.status == "ok" && it.user != null) {
                onSuccess(it.user!!)
            }
        }
    }

    private fun observeUsers(user: InstagramUser, onSuccess: () -> Unit) {
        userPk = user.pk
        databaseRepository.fetchUser(userPk) {
            if (it == null) insertUser() else
                currentUserCount = it.count
            onSuccess()
        }
    }

    private fun destroy() {
        NotificationProvider.cancelNotification()
        insertUser(currentUserCount + currentCount)
        stopSelf()
    }

    private fun insertUser(count: Int = 0) {
        databaseRepository.insertUser(UserData(userPk, count))
    }

}