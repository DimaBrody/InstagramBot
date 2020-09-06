package com.bot.insta.internal

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.bot.insta.tools.receivers.ServiceReceiver
import java.util.concurrent.TimeUnit

fun scheduleBot(context: Context, timePass: Long = TimeUnit.HOURS.toMillis(2)) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val receiverIntent = Intent(context, ServiceReceiver::class.java)
    val pi = PendingIntent.getBroadcast(context, 0, receiverIntent, 0)

    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + timePass,
            pi
        )
        return
    }
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timePass, pi)
}

fun scheduleInstant(context: Context) = scheduleBot(context,0)

fun stopSchedule(context: Context) {
    val pendingIntent =
        PendingIntent.getBroadcast(context, 0, Intent(context, ServiceReceiver::class.java), 0)
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
}