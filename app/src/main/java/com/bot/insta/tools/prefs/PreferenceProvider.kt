package com.bot.insta.tools.prefs

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.bot.insta.data.request.payload.InstagramLoginResult
import com.bot.insta.data.variables.AppValues.DEFAULT_HASH
import com.bot.insta.data.variables.AppValues.DEFAULT_LIST
import com.bot.insta.data.variables.AppValues.HASH
import com.bot.insta.data.variables.AppValues.LIST
import com.bot.insta.internal.delegates.boolean
import com.bot.insta.internal.delegates.int
import com.bot.insta.internal.delegates.long
import com.bot.insta.internal.delegates.string
import com.bot.insta.tools.services.accessibility.ModeService.Companion.UNFOLLOW_STRATEGY
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit

class PreferenceProvider(
    private val mContext: Context
) {

    private val context: Context
        get() = mContext.applicationContext

    private val prefs: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(context)

    var username: String by prefs.string()

    var password: String by prefs.string()

    var isLoggedIn: Boolean by prefs.boolean()

    var isLaunchOnStart: Boolean by prefs.boolean()

    var isAutomatic: Boolean by prefs.boolean(true)

    var currentMode: String by prefs.string()

    var currentFollowingTime: Long by prefs.long(TimeUnit.HOURS.toMillis(1))

    var currentFollowersTime: Long by prefs.long(TimeUnit.HOURS.toMillis(24))

    var urlAvatar: String by prefs.string()

    var userPk: Long by prefs.long()

    var peopleCount: Int by prefs.int(3)

    var currentStrategy: Int by prefs.int(UNFOLLOW_STRATEGY)


    private inline fun <reified T : Any> getPreference(key: String, defValue: Long = 0L): T =
        with(prefs) {
            with(T::class.java) {
                when {
                    isAssignableFrom(Long::class.java) -> getLong(key, defValue) as T
                    isAssignableFrom(Integer::class.java) -> getInt(key, 0) as T
                    isAssignableFrom(String::class.java) -> getString(key, "") as T
                    isAssignableFrom(Boolean::class.java) -> getBoolean(key, false) as T
                    else -> throw IllegalArgumentException("Wrong type in preferences")
                }
            }
        }


    private fun savePreference(key: String, value: Any?) {
        prefs.edit().apply {
            when (value) {
                is Long -> putLong(key, value)
                is Int -> putInt(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
            }
        }.apply()
    }

    fun saveProfile(user: InstagramLoginResult){
        urlAvatar = user.logged_in_user?.profile_pic_url?:""
        userPk = user.logged_in_user?.pk?:0
    }

    var currentList: List<String>
        set(value) {
            val sb = StringBuilder()
            for (element in value) {
                sb.append(element).append(",")
            }
            prefs.edit().putString(LIST, sb.toString()).apply()
        }
        get() = prefs.getString(LIST, DEFAULT_LIST)?.split(",")?.filter { it != "" }
            ?: listOf()

    var hashtagList: List<String>
        set(value) {
            val sb = StringBuilder()
            for (element in value) {
                sb.append(element).append(",")
            }
            prefs.edit().putString(HASH, sb.toString()).apply()
        }
        get() = prefs.getString(
            HASH,
            DEFAULT_HASH
        )?.split(",")?.filter { it != "" }?.map { if (it[0] != '#') "#$it" else it }
            ?: listOf()

}