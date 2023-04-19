package com.aroman.mimwallet.domain.model

data class NoticePortfolioUiState(
    val noticeList: List<NoticePortfolio>,
    val nextTimerInMillis: Long?
)