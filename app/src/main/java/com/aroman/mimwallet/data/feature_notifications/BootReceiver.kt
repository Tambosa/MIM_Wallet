package com.aroman.mimwallet.data.feature_notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.aroman.mimwallet.domain.repository.NoticePortfolioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class BootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var noticePortfolioRepository: NoticePortfolioRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            CoroutineScope(Dispatchers.IO).launch {
                noticePortfolioRepository.getAll().forEach { notice ->
                    if (notice.isActive) {
                        PortfolioNotificationManager.startReminder(
                            context = context,
                            reminderTimeHours = notice.hour,
                            reminderTimeMinutes = notice.minute,
                            reminderId = notice.id
                        )
                    }
                }
            }
        }
    }
}