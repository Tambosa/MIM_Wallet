package com.aroman.mimwallet.domain.repository

import com.aroman.mimwallet.domain.model.NoticePortfolio

interface NoticePortfolioRepository {
    suspend fun getAll(): List<NoticePortfolio>
    suspend fun saveNotice(noticePortfolio: NoticePortfolio)
    suspend fun updateNotice(noticePortfolio: NoticePortfolio)
    suspend fun deleteNotice(noticePortfolio: NoticePortfolio)
}