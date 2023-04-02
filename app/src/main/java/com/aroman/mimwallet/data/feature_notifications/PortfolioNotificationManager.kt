package com.aroman.mimwallet.data.feature_notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Handler
import android.widget.Toast
import com.aroman.mimwallet.R
import java.util.*
import java.util.concurrent.TimeUnit

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
        val timer = calendar.timeInMillis - System.currentTimeMillis()
        val notificationText = context.resources.getString(
            R.string.notification_toast_template,
            TimeUnit.MILLISECONDS.toHours(timer),
            TimeUnit.MILLISECONDS.toMinutes(timer) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timer))
        )
        Handler(context.mainLooper).post {
            Toast.makeText(
                context,
                notificationText,
                Toast.LENGTH_SHORT
            )
                .show()
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