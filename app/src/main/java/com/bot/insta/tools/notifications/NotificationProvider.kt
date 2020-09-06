package com.bot.insta.tools.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.bot.insta.App
import com.bot.insta.R
import com.bot.insta.internal.simpleTry
import kotlin.math.roundToInt

object NotificationProvider {

    private const val ID = 123

    private lateinit var context: Context

    private lateinit var mNotificationBuilder: Notification.Builder

    private lateinit var mNotification : Notification

    private val notificationManager: NotificationManager
        get() = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private var globalCount: Int = 0

    private var lastCurrent: Int = 0

    fun init(context: Context){
        this.context = context.applicationContext

        simpleTry { createChannel() }
        initNotifications()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(App.CHANNEL_ID) == null) {
                val name = context.getString(R.string.notification_channel)
                val descriptionText = context.getString(R.string.notification_channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(App.CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    private fun initNotifications(){
        mNotificationBuilder = Notification.Builder(context)
    }

    fun createNotification(globalCount: Int){
        this.globalCount = globalCount

        mNotificationBuilder.apply {
            setOngoing(true)
            setSmallIcon(R.drawable.ic_people)
            setContentTitle("Instagram Following")
            setContentText("0/$globalCount")
            setProgress(100,0,false)
        }

        mNotification = mNotificationBuilder.build()
        notificationManager.notify(ID,mNotification)
    }

    fun updateNotification(currentCount: Int){
        lastCurrent = currentCount+1

        val progress = (currentCount.toFloat()/ globalCount.toFloat())*100

        mNotificationBuilder.setProgress(100,progress.roundToInt(),false)
            .setContentText("$currentCount/$globalCount")
        mNotification = mNotificationBuilder.build()

        notificationManager.notify(ID, mNotification)
    }

    fun cancelNotification(){
//        notificationManager.cancel(ID)
        mNotificationBuilder = Notification.Builder(context)

        mNotificationBuilder.apply {
            setSmallIcon(R.drawable.ic_people)
            setContentTitle("Instagram Following")
            setContentText("($lastCurrent/$globalCount) Done!")
        }

        mNotification = mNotificationBuilder.build()
        notificationManager.notify(ID, mNotification)
    }

}