package com.aroman.mimwallet.presentation_compose.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aroman.mimwallet.domain.model.NoticePortfolio
import com.aroman.mimwallet.domain.repository.NoticePortfolioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComposeNoticePortfolioViewModel @Inject constructor(
    private val noticePortfolioRepo: NoticePortfolioRepository
) : ViewModel() {
    private val _noticePortfolioList = MutableStateFlow<List<NoticePortfolio>>(listOf())
    val noticePortfolioList = _noticePortfolioList.asStateFlow()

    fun getNoticePortfolioList() {
        viewModelScope.launch {
            _noticePortfolioList.value = noticePortfolioRepo.getAll()
        }
    }

    fun insertNoticePortfolio(noticePortfolio: NoticePortfolio) {
        viewModelScope.launch {
            noticePortfolioRepo.saveNotice(noticePortfolio)
            _noticePortfolioList.value = noticePortfolioRepo.getAll()
        }
    }

    fun updateNoticePortfolio(noticePortfolio: NoticePortfolio) {
        viewModelScope.launch {
            noticePortfolioRepo.updateNotice(noticePortfolio)
            _noticePortfolioList.value = noticePortfolioRepo.getAll()
        }
    }

    fun deleteNoticePortfolio(noticePortfolio: NoticePortfolio) {
        viewModelScope.launch {
            noticePortfolioRepo.deleteNotice(noticePortfolio)
            _noticePortfolioList.value = noticePortfolioRepo.getAll()
        }
    }
}