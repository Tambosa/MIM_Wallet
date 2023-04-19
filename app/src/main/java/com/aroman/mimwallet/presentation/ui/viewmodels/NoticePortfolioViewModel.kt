package com.aroman.mimwallet.presentation.ui.viewmodels

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aroman.mimwallet.domain.model.NoticePortfolio
import com.aroman.mimwallet.domain.model.NoticePortfolioUiState
import com.aroman.mimwallet.domain.model.NoticePortfolioUiEvent
import com.aroman.mimwallet.domain.repository.NoticePortfolioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NoticePortfolioViewModel @Inject constructor(
    private val noticePortfolioRepo: NoticePortfolioRepository
) : ViewModel() {

    private val _noticePortfolioUiState = MutableStateFlow(
        NoticePortfolioUiState(
            listOf(),
            null
        )
    )

    val noticePortfolioState = _noticePortfolioUiState.asStateFlow()

    init {
        tickerFlow().launchIn(viewModelScope)
    }

    private fun tickerFlow() = flow {
        while (true) {
            emit(Unit)
            updateTimer()
            delay(60000)
        }
    }

    fun onEvent(event: NoticePortfolioUiEvent) {
        when (event) {
            is NoticePortfolioUiEvent.ShowData -> {
                getNoticePortfolioList()
            }

            is NoticePortfolioUiEvent.AddItem -> {
                insertNoticePortfolio(event.noticePortfolio)
            }

            is NoticePortfolioUiEvent.UpdateItem -> {
                updateNoticePortfolio(event.noticePortfolio)
            }

            is NoticePortfolioUiEvent.DeleteItem -> {
                deleteNoticePortfolio(event.noticePortfolio)
            }
        }
    }

    private fun getNoticePortfolioList() {
        viewModelScope.launch {
            updateNoticeListValue()
            updateTimer()
        }
    }

    private fun insertNoticePortfolio(noticePortfolio: NoticePortfolio) {
        viewModelScope.launch {
            noticePortfolioRepo.saveNotice(noticePortfolio)
            updateNoticeListValue()
            updateTimer()
        }
    }

    private fun updateNoticePortfolio(noticePortfolio: NoticePortfolio) {
        viewModelScope.launch {
            noticePortfolioRepo.updateNotice(noticePortfolio)
            updateNoticeListValue()
            updateTimer()
        }
    }

    private fun deleteNoticePortfolio(noticePortfolio: NoticePortfolio) {
        viewModelScope.launch {
            noticePortfolioRepo.deleteNotice(noticePortfolio)
            updateNoticeListValue()
            updateTimer()
        }
    }

    private suspend fun updateNoticeListValue() {
        _noticePortfolioUiState.value =
            _noticePortfolioUiState.value.copy(noticeList = noticePortfolioRepo.getAll())
    }

    private fun updateTimer() {
        _noticePortfolioUiState.value = _noticePortfolioUiState.value.copy(
            nextTimerInMillis = noticePortfolioState.value.noticeList
                .filter { it.isActive }
                .minOfOrNull {
                    if (
                        Calendar.getInstance(Locale.ENGLISH).apply {
                            set(Calendar.HOUR_OF_DAY, it.hour)
                            set(Calendar.MINUTE, it.minute)
                        }.timeInMillis - System.currentTimeMillis() > 0
                    ) {
                        Calendar.getInstance(Locale.ENGLISH).apply {
                            set(Calendar.HOUR_OF_DAY, it.hour)
                            set(Calendar.MINUTE, it.minute)
                        }.timeInMillis - System.currentTimeMillis()
                    } else {
                        Calendar.getInstance(Locale.ENGLISH).apply {
                            set(Calendar.HOUR_OF_DAY, it.hour)
                            set(Calendar.MINUTE, it.minute)
                            add(Calendar.DATE, 1)
                        }.timeInMillis - System.currentTimeMillis()
                    }
                })
    }
}
