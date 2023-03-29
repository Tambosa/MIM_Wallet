package com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.aroman.mimwallet.data.feature_notifications.PortfolioNotificationManager.CHANNEL_ID
import com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.components.DisplayableInsertNoticePortfolio
import com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.components.DisplayableNoticePortfolioItem
import com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.components.NotificationsTitle
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeNoticePortfolioViewModel
import com.aroman.mimwallet.utils.createNotificationChannel
import com.aroman.mimwallet.utils.isNotificationAllowed
import java.util.concurrent.TimeUnit

@Composable
fun PortfolioNotificationsScreen(
    noticePortfolioViewModel: ComposeNoticePortfolioViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var hasNotificationPermission by remember(isNotificationAllowed(context))
    InitNotificationPermissions(context, hasNotificationPermission) {
        hasNotificationPermission = it
    }
    val noticePortfolioList by noticePortfolioViewModel.noticePortfolioList.collectAsState()
    val nextTimerInMillis by noticePortfolioViewModel.nextTimerInMillis.collectAsState()
    LaunchedEffect(Unit) {
        noticePortfolioViewModel.getNoticePortfolioList()
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        if (noticePortfolioList.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                NotificationsTitle()
                DisplayableInsertNoticePortfolio(viewModel = noticePortfolioViewModel)
            }
        } else {
            Column {
                NotificationsTitle()
                nextTimerInMillis?.let { NextNotificationTimer(it) }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    items(
                        count = noticePortfolioList.size,
                        key = { index -> noticePortfolioList[index].id },
                        itemContent = { index ->
                            DisplayableNoticePortfolioItem(
                                noticePortfolioList[index],
                                noticePortfolioViewModel
                            )
                            if (index == noticePortfolioList.size - 1) {
                                DisplayableInsertNoticePortfolio(viewModel = noticePortfolioViewModel)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NextNotificationTimer(nextTimerInMillis: Long) {
    val formattedTimer = String.format(
        "%2d hours and %2d minutes",
        TimeUnit.MILLISECONDS.toHours(nextTimerInMillis),
        TimeUnit.MILLISECONDS.toMinutes(nextTimerInMillis) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(nextTimerInMillis))
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "Next notification in $formattedTimer")
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