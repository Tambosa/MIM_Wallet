package com.aroman.mimwallet.domain.model.ui

import com.aroman.mimwallet.domain.model.NoticePortfolio

data class NoticePortfolioUiState(
    val noticeList: List<NoticePortfolio>,
    val nextTimerInMillis: Long?
)