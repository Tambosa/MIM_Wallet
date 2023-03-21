package com.aroman.mimwallet.data.feature_notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import java.util.*

object PortfolioNotificationManager {
    const val CHANNEL_ID = "001"
    const val CHANNEL_NAME = "Portfolio daily notification"

    fun startReminder(
        context: Context,
        reminderTimeHours: Int,
        reminderTimeMinutes: Int,
        reminderId: Int,
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java).let {
            it.putExtra("reminderId", reminderId)
            it.putExtra("reminderTimeHours", reminderTimeHours)
            it.putExtra("reminderTimeMinutes", reminderTimeMinutes)
            PendingIntent.getBroadcast(
                context.applicationContext,
                reminderId,
                it,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val calendar = Calendar.getInstance(Locale.ENGLISH).apply {
            set(Calendar.HOUR_OF_DAY, reminderTimeHours)
            set(Calendar.MINUTE, reminderTimeMinutes)
        }
        if (Calendar.getInstance(Locale.ENGLISH)
                .apply { add(Calendar.MINUTE, 1) }.timeInMillis - calendar.timeInMillis > 0
        ) {
            calendar.add(Calendar.DATE, 1)
        }

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(calendar.timeInMillis, intent),
            intent
        )
    }

    fun stopReminder(
        context: Context,
        reminderId: Int
    ) {
        Log.d("@@@", "stopReminder: $reminderId")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                reminderId,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        alarmManager.cancel(intent)
    }
}