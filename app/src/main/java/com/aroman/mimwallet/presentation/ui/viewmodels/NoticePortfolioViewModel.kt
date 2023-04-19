package com.aroman.mimwallet.presentation.ui.viewmodels

import android.content.Context
import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aroman.mimwallet.data.feature_notifications.PortfolioNotificationManager
import com.aroman.mimwallet.domain.model.NoticePortfolio
import com.aroman.mimwallet.domain.model.NoticePortfolioState
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

    private val _noticePortfolioState = MutableStateFlow(
        NoticePortfolioState(
            listOf(),
            null
        )
    )

    val noticePortfolioState = _noticePortfolioState.asStateFlow()

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

    fun getNoticePortfolioList() {
        viewModelScope.launch {
            updateNoticeListValue()
            updateTimer()
        }
    }

    fun insertNoticePortfolio(noticePortfolio: NoticePortfolio) {
        viewModelScope.launch {
            noticePortfolioRepo.saveNotice(noticePortfolio)
            updateNoticeListValue()
            updateTimer()
        }
    }

    fun updateNoticePortfolio(noticePortfolio: NoticePortfolio) {
        viewModelScope.launch {
            noticePortfolioRepo.updateNotice(noticePortfolio)
            updateNoticeListValue()
            updateTimer()
        }
    }

    fun deleteNoticePortfolio(context: Context, noticePortfolio: NoticePortfolio) {
        viewModelScope.launch {
            PortfolioNotificationManager.stopReminder(
                context = context,
                reminderId = noticePortfolio.id
            )
            noticePortfolioRepo.deleteNotice(noticePortfolio)
            updateNoticeListValue()
            updateTimer()
        }
    }

    private suspend fun updateNoticeListValue() {
        _noticePortfolioState.value =
            _noticePortfolioState.value.copy(noticeList = noticePortfolioRepo.getAll())
    }

    private fun updateTimer() {
        _noticePortfolioState.value = _noticePortfolioState.value.copy(
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
