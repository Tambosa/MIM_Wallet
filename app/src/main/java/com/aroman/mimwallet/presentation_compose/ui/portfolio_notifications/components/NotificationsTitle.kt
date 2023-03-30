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

@Composable
fun NotificationsTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Portfolio notifications",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}