package com.aroman.mimwallet.data.feature_notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.aroman.mimwallet.R
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.data.feature_notifications.PortfolioNotificationManager.CHANNEL_ID
import com.aroman.mimwallet.domain.repository.NoticePortfolioRepository
import com.aroman.mimwallet.domain.use_case.get_portfolio.GetPortfolioUseCase
import com.aroman.mimwallet.utils.let3
import com.aroman.mimwallet.utils.sendNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getPortfolioUseCase: GetPortfolioUseCase

    @Inject
    lateinit var noticePortfolioRepository: NoticePortfolioRepository

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager
        val reminderId = intent.extras?.getInt("reminderId") ?: 0
        val reminderTimeHours = intent.extras?.getInt("reminderTimeHours")
        val reminderTimeMinutes = intent.extras?.getInt("reminderTimeMinutes")

        getPortfolioUseCase().onEach { result ->
            if (result is ViewState.Success) {
                val totalPrice = String.format("$%.2f", result.successData.totalPrice)
                val percent = DecimalFormat("0.##'%'").apply {
                    roundingMode = RoundingMode.CEILING
                }.format(result.successData.totalPercentChange24h)

                notificationManager.sendNotification(
                    applicationContext = context,
                    channelId = CHANNEL_ID,
                    notificationId = reminderId,
                    title = context.getString(R.string.portfolio_update),
                    text = context.getString(R.string.notification_template, totalPrice, percent),
                    icon = R.drawable.baseline_currency_bitcoin_24
                )
            }
        }.launchIn(CoroutineScope(Dispatchers.IO))

        let3(reminderId, reminderTimeHours, reminderTimeMinutes)
        { _reminderId, _reminderTimeHours, _reminderTimeMinutes ->
            CoroutineScope(Dispatchers.IO).launch {
                val list = noticePortfolioRepository.getAll()
                if (list.any {
                        it.id == _reminderId &&
                                it.hour == reminderTimeHours &&
                                it.minute == reminderTimeMinutes &&
                                it.isActive
                    }) {
                    PortfolioNotificationManager.startReminder(
                        context,
                        _reminderTimeHours,
                        _reminderTimeMinutes,
                        _reminderId
                    )
                }
            }
        }
    }
}