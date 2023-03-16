package com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.feature.PortfolioNotificationManager
import com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.feature.PortfolioNotificationManager.CHANNEL_ID
import com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.feature.PortfolioNotificationManager.CHANNEL_NAME
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeNotificationViewModel
import java.util.*

@Composable
fun PortfolioNotificationsScreen(notificationViewModel: ComposeNotificationViewModel) {
    val context = LocalContext.current
    var hasNotificationPermission by remember {
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
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(), onResult = { isGranted ->
            hasNotificationPermission = isGranted
        }
    )
    LaunchedEffect(Unit) {
        createNotificationChannel(CHANNEL_ID, context)
        if (!hasNotificationPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                PortfolioNotificationManager.startReminder(
                    context = context,
                    reminderTimeHours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    reminderTimeMinutes = Calendar.getInstance().get(Calendar.MINUTE) + 1,
                )
            }) {
                Text(text = "Launch Notification in 1 minute")
            }
        }
    }
}

fun createNotificationChannel(channelId: String, context: Context) {
    (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
        NotificationChannel(
            channelId,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
    )
}