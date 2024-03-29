package com.aroman.mimwallet.domain.model.ui

import com.aroman.mimwallet.domain.model.NoticePortfolio

sealed class NoticePortfolioUiEvent {
    data class ShowData(val oldState: NoticePortfolioUiState) : NoticePortfolioUiEvent()
    data class AddItem(val noticePortfolio: NoticePortfolio) : NoticePortfolioUiEvent()
    data class UpdateItem(val noticePortfolio: NoticePortfolio) : NoticePortfolioUiEvent()
    data class DeleteItem(val noticePortfolio: NoticePortfolio) : NoticePortfolioUiEvent()
}
