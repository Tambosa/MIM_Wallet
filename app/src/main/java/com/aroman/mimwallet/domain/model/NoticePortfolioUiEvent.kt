package com.aroman.mimwallet.domain.model

sealed class NoticePortfolioUiEvent {
    data class ShowData(val oldState: NoticePortfolioState) : NoticePortfolioUiEvent()
    data class AddItem(val noticePortfolio: NoticePortfolio) : NoticePortfolioUiEvent()
    data class UpdateItem(val noticePortfolio: NoticePortfolio) : NoticePortfolioUiEvent()
    data class DeleteItem(val noticePortfolio: NoticePortfolio) : NoticePortfolioUiEvent()
}
