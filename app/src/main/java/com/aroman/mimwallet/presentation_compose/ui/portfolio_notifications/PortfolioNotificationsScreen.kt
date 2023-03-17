package com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.feature.PortfolioNotificationManager.CHANNEL_ID
import com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.feature.PortfolioNotificationManager.CHANNEL_NAME
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioNotificationsScreen() {
    val context = LocalContext.current
    var hasNotificationPermission by remember(isNotificationAllowed(context))
    InitNotificationPermissions(context, hasNotificationPermission) {
        hasNotificationPermission = it
    }
    var pickedTime by remember { mutableStateOf(LocalTime.NOON) }
    val clockDialogState = rememberUseCaseState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                clockDialogState.show()
            }) {
                Text(text = "Pick time")
            }
            Text(
                text = DateTimeFormatter
                    .ofPattern("hh:mm")
                    .format(pickedTime)
            )
        }
        ClockDialog(
            state = clockDialogState,
            config = ClockConfig(
                defaultTime = pickedTime,
                is24HourFormat = true
            ),
            selection = ClockSelection.HoursMinutes { hours, minutes ->
                pickedTime = LocalTime.of(hours, minutes)
                Log.d("@@@", "PortfolioNotificationsScreen: $pickedTime")
            }
        )
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Button(onClick = {
//                PortfolioNotificationManager.startReminder(
//                    context = context,
//                    reminderTimeHours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
//                    reminderTimeMinutes = Calendar.getInstance().get(Calendar.MINUTE) + 2,
//                )
//            }) {
//                Text(text = "Launch Notification in 1 minute")
//            }
//        }
    }
}

@Composable
private fun InitNotificationPermissions(
    context: Context,
    isAllowed: Boolean,
    hasNotificationPermission: (Boolean) -> Unit,
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { result -> hasNotificationPermission(result) }
    )
    LaunchedEffect(Unit) {
        createNotificationChannel(CHANNEL_ID, context)
        if (!isAllowed) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

private fun isNotificationAllowed(context: Context): () -> MutableState<Boolean> = {
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

fun createNotificationChannel(channelId: String, context: Context) {
    (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
        NotificationChannel(
            channelId,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
    )
}