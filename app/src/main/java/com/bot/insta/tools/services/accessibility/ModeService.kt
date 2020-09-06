package com.bot.insta.tools.services.accessibility

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.bot.insta.components.dependencies.prefs
import com.bot.insta.internal.instagram.AutoService
import com.bot.insta.internal.instagram.AutoServiceImpl
import com.bot.insta.internal.model.Mode

class ModeService : Service() {

    private lateinit var autoService: AutoService

    private var currentStrategy: Int = -1

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)

        currentStrategy = intent?.getIntExtra(STRATEGY_EXTRA, 2) ?: 2

        autoService = AutoServiceImpl(applicationContext)

        autoService.apply {
            iterateScreen(true,{
                launchApplication()
                iterateScreen {
                    createCompletedChain()
                }
            }) {
                checkOpenedScreen(
                    onFollowing = onFollowing,
                    onFollowers = onFollowers
                ) {
                    createCompletedChain()
                }
            }

        }
    }

    private fun AutoService.createCompletedChain() {
        if (currentStrategy == SUBSCIBE_STRATEGY) {
            openSearch {
                clickSearch {
                    printAndSearch {
                        clickOnName {
                            clickOnSubs {
                                subscribeAndSwipe {}
                            }
                        }
                    }
                }
            }
        } else {
            openProfile {
                clickFollowing {
                    unSubscribeAndSwipe {}
                }
            }
        }
    }

    private val AutoService.onFollowing: () -> Unit
        get() = {
            if (currentStrategy == UNFOLLOW_STRATEGY)
                unSubscribeAndSwipe {}
            else createCompletedChain()
        }

    private val AutoService.onFollowers: () -> Unit
        get() = {
            if (currentStrategy == SUBSCIBE_STRATEGY)
                subscribeAndSwipe {}
            else createCompletedChain()
        }

    private val mode: Mode
        get() = prefs.currentMode.let {
            Mode.valueOf(it)
        }

    companion object {
        const val STRATEGY_EXTRA = "sub"

        const val SUBSCIBE_STRATEGY = 1
        const val UNFOLLOW_STRATEGY = 2
    }

}