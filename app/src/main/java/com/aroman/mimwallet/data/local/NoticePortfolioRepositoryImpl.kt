package com.aroman.mimwallet.data.local

import com.aroman.mimwallet.data.local.dao.NoticeDao
import com.aroman.mimwallet.data.local.tables.NoticePortfolioRoomEntity
import com.aroman.mimwallet.data.local.tables.toNoticePortfolio
import com.aroman.mimwallet.domain.model.NoticePortfolio
import com.aroman.mimwallet.domain.repository.NoticePortfolioRepository

class NoticePortfolioRepositoryImpl(private val dao: NoticeDao) : NoticePortfolioRepository {
    override suspend fun getAll(): List<NoticePortfolio> = dao.getAllNotice().map { it.toNoticePortfolio() }

    override suspend fun saveNotice(noticePortfolio: NoticePortfolio) {
        dao.insertNotice(NoticePortfolioRoomEntity(noticePortfolio.id, noticePortfolio.hour, noticePortfolio.minute, noticePortfolio.isActive))
    }

    override suspend fun updateNotice(noticePortfolio: NoticePortfolio) {
        dao.updateNotice(NoticePortfolioRoomEntity(noticePortfolio.id, noticePortfolio.hour, noticePortfolio.minute, noticePortfolio.isActive))
    }

    override suspend fun deleteNotice(noticePortfolio: NoticePortfolio) {
        dao.deleteNotice(NoticePortfolioRoomEntity(noticePortfolio.id, noticePortfolio.hour, noticePortfolio.minute, noticePortfolio.isActive))
    }
}