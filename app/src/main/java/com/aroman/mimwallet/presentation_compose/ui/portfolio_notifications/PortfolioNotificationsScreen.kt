package com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.aroman.mimwallet.data.feature_notifications.PortfolioNotificationManager.CHANNEL_ID
import com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.components.DisplayableInsertNoticePortfolio
import com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.components.DisplayableNoticePortfolioItem
import com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.components.DisplayableNotificationsTitle
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeNoticePortfolioViewModel
import com.aroman.mimwallet.utils.createNotificationChannel
import com.aroman.mimwallet.utils.isNotificationAllowed

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
                DisplayableNotificationsTitle()
                DisplayableInsertNoticePortfolio(viewModel = noticePortfolioViewModel)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                items(
                    count = noticePortfolioList.size,
                    key = { index -> noticePortfolioList[index].id },
                    itemContent = { index ->
                        if (index == 0) {
                            DisplayableNotificationsTitle()
                        }
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