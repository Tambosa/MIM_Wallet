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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.aroman.mimwallet.R
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeNotificationViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

@Composable
fun PortfolioNotificationsScreen(notificationViewModel: ComposeNotificationViewModel) {
    val portfolioState by notificationViewModel.portfolio.collectAsState()
    val shouldShowNotification by notificationViewModel.shouldShowNotification.collectAsState()
    val context = LocalContext.current
    val channelId = "001"
    val notificationId = 0
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
        createNotificationChannel(channelId, context)
        if (!hasNotificationPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    if (portfolioState is ViewState.Success && shouldShowNotification) {
        with(portfolioState as ViewState.Success) {
            val totalPrice = String.format("$%.2f", successData.totalPrice)
            val percent = DecimalFormat("0.##'%'").apply {
                roundingMode = RoundingMode.CEILING
            }.format(successData.totalPercentChange24h)
            if (hasNotificationPermission) {
                showSimpleNotification(
                    context,
                    channelId,
                    notificationId,
                    "Portfolio Update",
                    "Total: $totalPrice 24h change: $percent",
                )
            }
            notificationViewModel.notificationFinished()
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
                notificationViewModel.requestNotification()
            }) {
                Text(text = "Activate notification")
            }
        }
    }
}

fun showSimpleNotification(
    context: Context,
    channelId: String,
    notificationId: Int,
    textTitle: String,
    textContent: String,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
) {
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.baseline_currency_bitcoin_24)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setPriority(priority)
        .setSmallIcon(R.drawable.baseline_currency_bitcoin_24)

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}

fun createNotificationChannel(channelId: String, context: Context) {
    (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
        NotificationChannel(
            channelId,
            "Portfolio daily notification",
            NotificationManager.IMPORTANCE_HIGH
        )
    )
}