package com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.feature

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.aroman.mimwallet.R
import com.aroman.mimwallet.domain.model.Portfolio
import com.aroman.mimwallet.presentation_compose.ComposeActivity
import com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.feature.PortfolioNotificationManager.CHANNEL_ID
import com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.feature.PortfolioNotificationManager.NOTIFICATION_ID
import java.math.RoundingMode
import java.text.DecimalFormat

class AlarmReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendReminderNotification(
            portfolioData = Portfolio(listOf(), totalPrice = 100.0, totalPercentChange24h = 4.2),
            applicationContext = context,
            channelId = CHANNEL_ID,
        )
        PortfolioNotificationManager.startReminder(context)
    }
}

fun NotificationManager.sendReminderNotification(
    applicationContext: Context,
    channelId: String,
    portfolioData: Portfolio,
) {
    val contentIntent = Intent(applicationContext, ComposeActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        1,
        contentIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val totalPrice = String.format("$%.2f", portfolioData.totalPrice)
    val percent = DecimalFormat("0.##'%'").apply {
        roundingMode = RoundingMode.CEILING
    }.format(portfolioData.totalPercentChange24h)

    val builder = NotificationCompat.Builder(applicationContext, channelId)
        .setContentTitle("Portfolio Update")
        .setContentText("Total: $totalPrice 24h change: $percent")
        .setSmallIcon(R.drawable.baseline_currency_bitcoin_24)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}