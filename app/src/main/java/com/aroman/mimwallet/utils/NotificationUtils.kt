package com.aroman.mimwallet.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.aroman.mimwallet.data.feature_notifications.PortfolioNotificationManager
import com.aroman.mimwallet.presentation_compose.ComposeActivity

fun isNotificationAllowed(context: Context): () -> MutableState<Boolean> = {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        )
    } else {
        mutableStateOf(true)
    }
}

fun createNotificationChannel(channelId: String, context: Context) {
    (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
        NotificationChannel(
            channelId,
            PortfolioNotificationManager.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
    )
}

fun NotificationManager.sendNotification(
    applicationContext: Context,
    channelId: String,
    notificationId: Int,
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
    notify(notificationId, builder.build())
}