package com.bot.insta.internal.instagram

interface AutoService {

    fun launchApplication()

    fun iterateScreen(isMainAppCheck: Boolean = false, onFailure: () -> Unit = {}, onSuccess: () -> Unit)

    fun openSearch(onFailure: () -> Unit = {}, onSuccess: () -> Unit)

    fun openProfile(onFailure: () -> Unit = {}, onSuccess: () -> Unit)

    fun clickSearch(onFailure: () -> Unit = {}, onSuccess: () -> Unit)

    fun clickFollowing(onFailure: () -> Unit = {}, onSuccess: () -> Unit)

    fun printAndSearch(onFailure: () -> Unit = {}, onSuccess: () -> Unit)

    fun clickOnName(onFailure: () -> Unit = {}, onSuccess: () -> Unit)

    fun clickOnSubs(onFailure: () -> Unit = {}, onSuccess: () -> Unit)

    fun subscribeAndSwipe(onFailure: () -> Unit = {},onSuccess: () -> Unit)

    fun checkOpenedScreen(
        onFollowing: () -> Unit = {},
        onFollowers: () -> Unit,
        onDefault: () -> Unit
    )

    fun unSubscribeAndSwipe(onFailure: () -> Unit = {},onSuccess: () -> Unit)

}