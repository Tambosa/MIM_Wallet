package com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.concurrent.TimeUnit

@Composable
fun NextNotificationTimer(nextTimerInMillis: Long) {
    val formattedTimer = String.format(
        "%2d hours and %2d minutes",
        TimeUnit.MILLISECONDS.toHours(nextTimerInMillis),
        TimeUnit.MILLISECONDS.toMinutes(nextTimerInMillis) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(nextTimerInMillis))
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Next notification in $formattedTimer",
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}