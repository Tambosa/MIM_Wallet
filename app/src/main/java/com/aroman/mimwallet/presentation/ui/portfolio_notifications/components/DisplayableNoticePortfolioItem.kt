package com.aroman.mimwallet.presentation.ui.portfolio_notifications.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aroman.mimwallet.R
import com.aroman.mimwallet.data.feature_notifications.PortfolioNotificationManager
import com.aroman.mimwallet.domain.model.NoticePortfolio
import com.aroman.mimwallet.domain.model.ui.NoticePortfolioUiEvent
import com.example.core_ui.theme.spacing
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
    onEvent: (NoticePortfolioUiEvent) -> Unit
) {
    val clockDialogState = rememberUseCaseState()
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.paddingExtraSmall, vertical = MaterialTheme.spacing.paddingSmall)
            .border(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                width = 1.dp
            )
            .padding(MaterialTheme.spacing.paddingSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        var checkedState by remember { mutableStateOf(noticePortfolio.isActive) }
        var pickedTime by remember {
            mutableStateOf(
                LocalTime.of(
                    noticePortfolio.hour, noticePortfolio.minute
                )
            )
        }
        Button(onClick = {
            clockDialogState.show()
        }) {
            Text(text = stringResource(id = R.string.pick_time))
        }
        Text(
            text = DateTimeFormatter.ofPattern("HH:mm").format(pickedTime),
        )
        Switch(checked = checkedState, onCheckedChange = {
            if (it) {
                PortfolioNotificationManager.startReminder(
                    context = context,
                    reminderTimeHours = noticePortfolio.hour,
                    reminderTimeMinutes = noticePortfolio.minute,
                    reminderId = noticePortfolio.id
                )
            } else {
                PortfolioNotificationManager.stopReminder(
                    context = context, reminderId = noticePortfolio.id
                )
            }
            checkedState = it
            onEvent(NoticePortfolioUiEvent.UpdateItem(noticePortfolio.copy(isActive = checkedState)))
        })
        IconButton(onClick = {
            PortfolioNotificationManager.stopReminder(
                context = context, reminderId = noticePortfolio.id
            )
            onEvent(NoticePortfolioUiEvent.DeleteItem(noticePortfolio))
        }) {
            Icon(Icons.Filled.Delete, stringResource(id = R.string.delete))
        }

        ClockDialog(state = clockDialogState, config = ClockConfig(
            defaultTime = pickedTime, is24HourFormat = true
        ), selection = ClockSelection.HoursMinutes { hours, minutes ->
            PortfolioNotificationManager.stopReminder(context, noticePortfolio.id)
            pickedTime = LocalTime.of(hours, minutes)
            onEvent(
                NoticePortfolioUiEvent.UpdateItem(
                    noticePortfolio.copy(
                        hour = hours,
                        minute = minutes
                    )
                )
            )
            if (noticePortfolio.isActive) {
                PortfolioNotificationManager.startReminder(
                    context = context,
                    reminderTimeHours = hours,
                    reminderTimeMinutes = minutes,
                    reminderId = noticePortfolio.id
                )
            }
        })
    }
}