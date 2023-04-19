package com.aroman.mimwallet.presentation.ui.portfolio_notifications

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.aroman.mimwallet.data.feature_notifications.PortfolioNotificationManager.CHANNEL_ID
import com.aroman.mimwallet.domain.model.NoticePortfolio
import com.aroman.mimwallet.presentation.ui.portfolio_notifications.components.DisplayableInsertNoticePortfolio
import com.aroman.mimwallet.presentation.ui.portfolio_notifications.components.DisplayableNoticePortfolioItem
import com.aroman.mimwallet.presentation.ui.portfolio_notifications.components.NotificationsTitle
import com.aroman.mimwallet.presentation.ui.portfolio_notifications.components.PortfolioNotificationsHeader
import com.aroman.mimwallet.utils.createNotificationChannel
import com.aroman.mimwallet.utils.isNotificationAllowed

@Composable
fun PortfolioNotificationsScreen(
    noticePortfolioList: List<NoticePortfolio>,
    nextTimerInMillis: Long?,
    getNoticePortfolioList: () -> Unit,
    insertNoticePortfolio: (NoticePortfolio) -> Unit,
    updateNoticePortfolio: (NoticePortfolio) -> Unit,
    deleteNoticePortfolio: (Context, NoticePortfolio) -> Unit,
) {
    val context = LocalContext.current
    var hasNotificationPermission by remember(isNotificationAllowed(context))
    InitNotificationPermissions(context, hasNotificationPermission) {
        hasNotificationPermission = it
    }
    val lazyListState = rememberLazyListState()
    val isFirstItemVisible by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (layoutInfo.totalItemsCount == 0) {
                true
            } else {
                visibleItemsInfo.first().index == 0
            }
        }
    }
    val cardAlpha by animateFloatAsState(
        targetValue = if (isFirstItemVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearOutSlowInEasing
        )
    )
    LaunchedEffect(Unit) {
        getNoticePortfolioList()
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        if (noticePortfolioList.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NotificationsTitle()
                DisplayableInsertNoticePortfolio(insertNoticePortfolio)
            }
        } else {
            Column {
                PortfolioNotificationsHeader(
                    nextTimerInMillis = nextTimerInMillis,
                    isExpanded = isFirstItemVisible,
                    cardAlpha = cardAlpha
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(
                        count = noticePortfolioList.size,
                        key = { index -> noticePortfolioList[index].id },
                        itemContent = { index ->
                            DisplayableNoticePortfolioItem(
                                noticePortfolioList[index],
                                updateNoticePortfolio,
                                deleteNoticePortfolio
                            )
                            if (index == noticePortfolioList.size - 1) {
                                DisplayableInsertNoticePortfolio(insertNoticePortfolio)
                            }
                        }
                    )
                }
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