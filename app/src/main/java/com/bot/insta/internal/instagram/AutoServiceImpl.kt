package com.bot.insta.internal.instagram

import android.content.Context
import android.widget.Toast
import com.bot.insta.internal.access.clickEverySubscribeButton
import com.bot.insta.internal.access.clickEveryUnFollowButton
import com.bot.insta.internal.access.searchAndPrint
import com.bot.insta.internal.access.searchView
import com.bot.insta.internal.conditions.*
import com.bot.insta.internal.instagram.functions.onSetup
import com.bot.insta.internal.instagram.root.CommandExecutor
import com.bot.insta.internal.iterateCurrentScreen
import com.bot.insta.internal.launchSettingsScreen
import com.bot.insta.internal.model.TabContainer
import com.bot.insta.internal.searchAndClick
import java.io.IOException
import java.util.concurrent.TimeUnit

class AutoServiceImpl(
    private val mContext: Context
) : AutoService {

    private val instagramTimeout = TimeUnit.SECONDS.toMillis(20)

    private val tabContainer = TabContainer()

    private val context: Context
        get() = mContext.applicationContext

    override fun subscribeAndSwipe(onFailure: () -> Unit, onSuccess: () -> Unit) {
        clickEverySubscribeButton(
            context,
            SUBSCRIBE_BUTTON,
            onTimeOut = onFailure,
            onComplete = onSuccess
        )
    }

    override fun unSubscribeAndSwipe(onFailure: () -> Unit, onSuccess: () -> Unit) {
        clickEveryUnFollowButton(
            context,
            UNFOLLOW_BUTTON,
            onTimeOut = onFailure,
            onComplete = onSuccess
        )
    }

    override fun openSearch(onFailure: () -> Unit, onSuccess: () -> Unit) {
        searchAndClick(
            context,
            BOTTOM_TAB,
            onSetup(tabContainer),
            onFailure,
            onComplete = onSuccess
        )
    }

    override fun openProfile(onFailure: () -> Unit, onSuccess: () -> Unit) {
        searchAndClick(
            context,
            PROFILE_TAB,
            onSetup(tabContainer),
            onFailure,
            onComplete = onSuccess
        )
    }

    override fun clickSearch(onFailure: () -> Unit, onSuccess: () -> Unit) {
        searchAndClick(
            context,
            SEARCH_ACTIONBAR,
            onTimeOut = onFailure,
            onComplete =  {
                try {
                    CommandExecutor.instance.executeTap()
                } catch (e: IOException){
                    e.printStackTrace()
                    Toast.makeText(context,"Failed, ${e.message}",Toast.LENGTH_LONG).show()
                }
                onSuccess()
            })
    }

    override fun printAndSearch(onFailure: () -> Unit, onSuccess: () -> Unit) {
        searchAndPrint(
            context,
            SEARCH_EDITTEXT,
            onTimeOut = onFailure,
            onComplete = onSuccess
        )
    }

    override fun clickOnName(onFailure: () -> Unit, onSuccess: () -> Unit) {
        searchAndClick(
            context,
            CURRENT_ACCOUNT,
            onTimeOut = onFailure,
            onComplete = onSuccess
        )
    }

    override fun clickOnSubs(onFailure: () -> Unit, onSuccess: () -> Unit) {
        searchAndClick(
            context,
            SUBSCRIBERS_BUTTON,
            onTimeOut = onFailure,
            onComplete = onSuccess
        )
    }

    override fun clickFollowing(onFailure: () -> Unit, onSuccess: () -> Unit) {
        searchAndClick(
            context,
            FOLLOWING_BUTTON,
            onTimeOut = onFailure,
            onComplete = onSuccess
        )
    }

    override fun iterateScreen(
        isMainAppCheck: Boolean,
        onFailure: () -> Unit,
        onSuccess: () -> Unit
    ) {
        iterateCurrentScreen(
            timeout = if(!isMainAppCheck) instagramTimeout else 750L,
            onFailed = onFailure,
            onSuccess = onSuccess
        )
    }

    override fun checkOpenedScreen(
        onFollowing: () -> Unit,
        onFollowers: () -> Unit,
        onDefault: () -> Unit
    ) {
        searchView(
            context,
            TITLE_HEADER,
            onTimeOut = onDefault,
            onFollowers = onFollowers,
            onFollowing = onFollowing
        )
    }

    override fun launchApplication() {
        launchSettingsScreen(context)
    }

}