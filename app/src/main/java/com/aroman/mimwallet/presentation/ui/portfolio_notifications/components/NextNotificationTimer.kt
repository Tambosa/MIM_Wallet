package com.aroman.mimwallet.presentation.ui.portfolio_notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aroman.mimwallet.R
import java.util.concurrent.TimeUnit

@Composable
fun NextNotificationTimer(nextTimerInMillis: Long) {
    val notificationText = stringResource(
        id = R.string.next_notification_template,
        TimeUnit.MILLISECONDS.toHours(nextTimerInMillis),
        TimeUnit.MILLISECONDS.toMinutes(nextTimerInMillis) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(nextTimerInMillis))
    )
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .padding(top = 8.dp ,bottom = 12.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = notificationText,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center
        )
    }
}