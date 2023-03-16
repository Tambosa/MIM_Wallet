package com.aroman.mimwallet.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.aroman.mimwallet.presentation_compose.ComposeActivity
import com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.feature.PortfolioNotificationManager

fun NotificationManager.sendNotification(
    applicationContext: Context,
    channelId: String,
    title: String,
    text: String,
    icon: Int,
) {
    val contentIntent = Intent(applicationContext, ComposeActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        1,
        contentIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    val builder = NotificationCompat.Builder(applicationContext, channelId)
        .setContentTitle(title)
        .setContentText(text)
        .setSmallIcon(icon)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
    notify(PortfolioNotificationManager.NOTIFICATION_ID, builder.build())
}