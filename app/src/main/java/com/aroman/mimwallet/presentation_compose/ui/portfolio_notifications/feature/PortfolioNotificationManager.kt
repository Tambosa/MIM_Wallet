package com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.feature

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import java.util.*

object PortfolioNotificationManager {
    const val NOTIFICATION_ID = 123
    const val CHANNEL_ID = "001"
    const val CHANNEL_NAME = "Portfolio daily notification"

    fun startReminder(
        context: Context,
        reminderTimeHours: Int = 8,
        reminderTimeMinutes: Int = 0,
        reminderId: Int = NOTIFICATION_ID,
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java).let {
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
            Log.d("@@@", "startReminder: tomorrow")
            calendar.add(Calendar.DATE, 1)
        }

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(calendar.timeInMillis, intent),
            intent
        )
    }

    fun stopReminder(
        context: Context,
        reminderId: Int = NOTIFICATION_ID
    ) {
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