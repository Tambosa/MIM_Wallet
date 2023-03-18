package com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.aroman.mimwallet.domain.model.NoticePortfolio
import com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.feature.PortfolioNotificationManager
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeNoticePortfolioViewModel
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DisplayableNoticePortfolioItem(
    noticePortfolio: NoticePortfolio,
    noticePortfolioViewModel: ComposeNoticePortfolioViewModel
) {
    val clockDialogState = rememberUseCaseState()
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        var checkedState by remember { mutableStateOf(noticePortfolio.isActive) }
        var pickedTime by remember {
            mutableStateOf(
                LocalTime.of(
                    noticePortfolio.hour,
                    noticePortfolio.minute
                )
            )
        }
        Button(onClick = {
            clockDialogState.show()
        }) {
            Text(text = "Pick time")
        }
        Text(
            text = DateTimeFormatter
                .ofPattern("HH:mm")
                .format(pickedTime),
        )
        Switch(
            checked = checkedState,
            onCheckedChange = {
                if (it) {
                    Log.d("@@@", "start: $noticePortfolio")
                    PortfolioNotificationManager.startReminder(
                        context = context,
                        reminderTimeHours = noticePortfolio.hour,
                        reminderTimeMinutes = noticePortfolio.minute,
                        reminderId = noticePortfolio.id
                    )
                } else {
                    Log.d("@@@", "stop: $noticePortfolio")
                    PortfolioNotificationManager.stopReminder(
                        context = context,
                        reminderId = noticePortfolio.id
                    )
                }
                checkedState = it
                noticePortfolio.isActive = checkedState
                noticePortfolioViewModel.updateNoticePortfolio(noticePortfolio)
            })
        IconButton(onClick = {
            noticePortfolioViewModel.deleteNoticePortfolio(noticePortfolio)
        }) {
            Icon(Icons.Filled.Delete, "delete")
        }

        ClockDialog(
            state = clockDialogState,
            config = ClockConfig(
                defaultTime = pickedTime,
                is24HourFormat = true
            ),
            selection = ClockSelection.HoursMinutes { hours, minutes ->
                PortfolioNotificationManager.stopReminder(context, noticePortfolio.id)

                pickedTime = LocalTime.of(hours, minutes)
                noticePortfolio.hour = hours
                noticePortfolio.minute = minutes
                noticePortfolio.isActive = false
                noticePortfolioViewModel.updateNoticePortfolio(noticePortfolio)

                if (noticePortfolio.isActive) {
                    PortfolioNotificationManager.startReminder(
                        context = context,
                        reminderTimeHours = noticePortfolio.hour,
                        reminderTimeMinutes = noticePortfolio.minute,
                        reminderId = noticePortfolio.id
                    )
                }
            }
        )
    }
}