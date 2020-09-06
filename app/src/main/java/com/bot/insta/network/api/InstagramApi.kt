package com.bot.insta.network.api

import android.annotation.SuppressLint
import com.bot.insta.components.dependencies.prefs
import com.bot.insta.data.request.InstagramFbLoginRequest
import com.bot.insta.data.request.InstagramFetchHeadersRequest
import com.bot.insta.data.request.InstagramLoginRequest
import com.bot.insta.data.request.InstagramSyncFeaturesRequest
import com.bot.insta.data.request.abstracts.InstagramRequest
import com.bot.insta.data.request.payload.InstagramFbLoginPayload
import com.bot.insta.data.request.payload.InstagramLoginPayload
import com.bot.insta.data.request.payload.InstagramLoginResult
import com.bot.insta.data.request.payload.InstagramSyncFeaturesPayload
import com.bot.insta.data.variables.AppValues
import com.bot.insta.data.variables.AppValues.DEVICE_EXPERIMENTS
import com.bot.insta.internal.log
import com.bot.insta.tools.hash.generateDeviceId
import com.bot.insta.tools.uuid.generateUuid
import okhttp3.*
import okhttp3.OkHttpClient.Builder
import java.io.IOException
import java.util.*


open class InstagramApi(
    private var username: String,
    private var password: String
) {

    private val cookieStore: HashMap<String?, Cookie?> = hashMapOf()

    private var deviceID: String = ""

     var uuid: String? = ""

    var rankToken: String? = ""

     var userID: Long = 0

    var lastResponse: Response? = null

    var client: OkHttpClient? = null

    private var isLoggedIn = false

    fun setup() {
        deviceID = generateDeviceId(username, password)
        uuid = generateUuid(true)
        client = Builder().
            cookieJar(object : CookieJar {
                override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>) {
                    if (cookies.isNotEmpty()) {
                        val iterator = cookies.iterator()

                        while (iterator.hasNext()) {
                            val cookie = iterator.next()
                            cookieStore[cookie.name()] = cookie
                        }
                    }
                }

                override fun loadForRequest(url: HttpUrl?): MutableList<Cookie> {
                    val validCookies = arrayListOf<Cookie>()
                    val iterator = cookieStore.entries.iterator()

                    while (iterator.hasNext()) {
                        iterator.next().value?.let {
                            if (it.expiresAt() >= System.currentTimeMillis())
                                validCookies.add(it)
                        }
                    }

                    return validCookies
                }
            }).build()
        username = prefs.username
        password = prefs.password
        isLoggedIn = prefs.isLoggedIn
    }

    @SuppressLint("DefaultLocale")
    @Throws(IOException::class)
    fun login(
        newUsername: String = this.username,
        newPassword: String = this.password
    ): InstagramLoginResult {
        this.username = newUsername
        this.password = newPassword

        val loginRequest = InstagramLoginPayload().apply {
            username = this@InstagramApi.username
            password = this@InstagramApi.password
            guid = uuid
            device_id = deviceID
            phone_id = generateUuid(true)
            login_attempt_account = 0
            _csrftoken = getOrFetchCsrf(null)
        }

        val loginResult = sendRequest(InstagramLoginRequest(loginRequest))
        if (loginResult.status.toLowerCase() == "ok") {
            userID = loginResult.logged_in_user!!.pk
            rankToken = "${userID}_$uuid"
            isLoggedIn = true

            val syncFeatures = InstagramSyncFeaturesPayload(
                _uuid = uuid!!,
                _csrftoken = getOrFetchCsrf(null)!!,
                _uid = userID,
                id = userID,
                experiments = AppValues.DEVICE_EXPERIMENTS
            )

            sendRequest(InstagramSyncFeaturesRequest(syncFeatures))
        }

        return loginResult
    }

    @Throws(IOException::class)
    fun loginFb(
        newUsername: String = this.username,
        newPassword: String = this.password
    ) : InstagramLoginResult {
        this.username = newUsername
        this.password = newPassword

        val loginRequest = InstagramFbLoginPayload(
            dryrun = true,
            adid = generateUuid(false),
            device_id = deviceID,
            fb_access_token = password,
            phone_id = generateUuid(false),
            waterfall_id = generateUuid(false)
        )

        val loginResult = sendRequest(InstagramFbLoginRequest(loginRequest))
        if(loginResult.status == "ok"){
            log(cookieStore.toString())
            userID = loginResult.logged_in_user?.pk?:0
            rankToken = "${userID}_$uuid"
            isLoggedIn = true

            val syncFeatures = InstagramSyncFeaturesPayload(
                _uuid = uuid!!,
                _csrftoken = getOrFetchCsrf(null)!!,
                _uid = userID,
                id = userID,
                experiments = DEVICE_EXPERIMENTS
            )

            sendRequest(InstagramSyncFeaturesRequest(syncFeatures))
        }

        return loginResult
    }


    @Throws(IOException::class)
    fun getOrFetchCsrf(url: HttpUrl?): String? {
        var cookie = getCsrfCookie(url)
        if (cookie == null) {
            sendRequest(InstagramFetchHeadersRequest())
            cookie = getCsrfCookie(url)
        }
        return cookie!!.value()
    }

    @Throws(IOException::class)
    fun getCsrfCookie(url: HttpUrl?): Cookie? {
        val var2: Iterator<*> =
            client!!.cookieJar().loadForRequest(url).iterator()
        var cookie: Cookie
        do {
            if (!var2.hasNext()) {
                return null
            }
            cookie = var2.next() as Cookie
        } while (!cookie.name().equals("csrftoken", ignoreCase = true))
        return cookie

    }

    @Throws(IOException::class)
    fun <T> sendRequest(request: InstagramRequest<T>): T {
        return if (!this.isLoggedIn && request.requiresLogin()) {
            throw IllegalStateException("Need to login first!")
        } else {
            request.api = this
            request.execute()
        }
    }


}