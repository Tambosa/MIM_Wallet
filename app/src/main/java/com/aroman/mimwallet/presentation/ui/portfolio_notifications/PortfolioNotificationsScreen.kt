package com.aroman.mimwallet.presentation.ui.portfolio_notifications

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.aroman.mimwallet.presentation.ui.viewmodels.NoticePortfolioViewModel

@Composable
fun PortfolioNotificationsScreen(noticePortfolioViewModel: NoticePortfolioViewModel = hiltViewModel()) {
    val noticePortfolioState by noticePortfolioViewModel.noticePortfolioState.collectAsState()
    val onNoticePortfolioUiEvent = remember { noticePortfolioViewModel::onEvent }

    PortfolioNotificationsContent(
        noticePortfolioUiState = noticePortfolioState,
        onEvent = onNoticePortfolioUiEvent
    )
}