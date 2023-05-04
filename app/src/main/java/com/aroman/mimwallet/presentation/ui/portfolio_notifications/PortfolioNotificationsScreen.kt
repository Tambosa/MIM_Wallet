package com.aroman.mimwallet.presentation.ui.portfolio_notifications

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aroman.mimwallet.R
import com.aroman.mimwallet.data.feature_notifications.PortfolioNotificationManager.CHANNEL_ID
import com.aroman.mimwallet.domain.model.NoticePortfolioUiEvent
import com.aroman.mimwallet.domain.model.NoticePortfolioUiState
import com.aroman.mimwallet.presentation.ui.portfolio_notifications.components.DisplayableInsertNoticePortfolio
import com.aroman.mimwallet.presentation.ui.portfolio_notifications.components.DisplayableNoticePortfolioItem
import com.aroman.mimwallet.presentation.ui.portfolio_notifications.components.NextNotificationTimer
import com.aroman.mimwallet.utils.createNotificationChannel
import com.aroman.mimwallet.utils.isNotificationAllowed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioNotificationsScreen(
    noticePortfolioUiState: NoticePortfolioUiState,
    onEvent: (NoticePortfolioUiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onEvent(NoticePortfolioUiEvent.ShowData(noticePortfolioUiState))
    }

    val context = LocalContext.current
    var hasNotificationPermission by remember(isNotificationAllowed(context))
    InitNotificationPermissions(context, hasNotificationPermission) {
        hasNotificationPermission = it
    }
    val listState = rememberLazyListState()
    val isScrolled by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    title = {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(id = R.string.notifications),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                            )
                        }
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
                )
            }
        ) { innerPadding ->
            if (noticePortfolioUiState.noticeList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding())
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    DisplayableInsertNoticePortfolio(onEvent = onEvent)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = innerPadding.calculateTopPadding())
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    AnimatedVisibility(noticePortfolioUiState.nextTimerInMillis != null && !isScrolled) {
                        NextNotificationTimer(
                            nextTimerInMillis = noticePortfolioUiState.nextTimerInMillis ?: 0
                        )
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        state = listState,
                        verticalArrangement = Arrangement.Center
                    ) {
                        items(count = noticePortfolioUiState.noticeList.size,
                            key = { index -> noticePortfolioUiState.noticeList[index].id },
                            itemContent = { index ->
                                DisplayableNoticePortfolioItem(
                                    noticePortfolioUiState.noticeList[index],
                                    onEvent
                                )
                                if (index == noticePortfolioUiState.noticeList.size - 1) {
                                    DisplayableInsertNoticePortfolio(onEvent)
                                    Spacer(modifier = Modifier.height(50.dp))
                                }
                            })
                    }
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
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
            onResult = { result -> hasNotificationPermission(result) })
    LaunchedEffect(Unit) {
        createNotificationChannel(CHANNEL_ID, context)
        if (!isAllowed) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}