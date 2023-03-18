package com.aroman.mimwallet.data.feature_notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.aroman.mimwallet.R
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.use_case.get_portfolio.GetPortfolioUseCase
import com.aroman.mimwallet.data.feature_notifications.PortfolioNotificationManager.CHANNEL_ID
import com.aroman.mimwallet.utils.sendNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getPortfolioUseCase: GetPortfolioUseCase
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        getPortfolioUseCase().onEach { result ->
            Log.d("@@@", "onReceive: ${result.toString()}")
            if (result is ViewState.Success) {
                val totalPrice = String.format("$%.2f", result.successData.totalPrice)
                val percent = DecimalFormat("0.##'%'").apply {
                    roundingMode = RoundingMode.CEILING
                }.format(result.successData.totalPercentChange24h)

                notificationManager.sendNotification(
                    applicationContext = context,
                    channelId = CHANNEL_ID,
                    title = "Portfolio Update",
                    text = "Total: $totalPrice 24h change: $percent",
                    icon = R.drawable.baseline_currency_bitcoin_24
                )
            }
        }.launchIn(CoroutineScope(Dispatchers.IO))

        PortfolioNotificationManager.startReminder(context)
    }
}