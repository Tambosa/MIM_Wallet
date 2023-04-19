package com.aroman.mimwallet.domain.model

data class NoticePortfolioState(
    val noticeList: List<NoticePortfolio>,
    val nextTimerInMillis: Long?
)