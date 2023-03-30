package com.aroman.mimwallet.presentation_compose.ui.viewmodels

import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aroman.mimwallet.data.feature_notifications.PortfolioNotificationManager
import com.aroman.mimwallet.domain.model.NoticePortfolio
import com.aroman.mimwallet.domain.repository.NoticePortfolioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ComposeNoticePortfolioViewModel @Inject constructor(
    private val noticePortfolioRepo: NoticePortfolioRepository
) : ViewModel() {

    private val _noticePortfolioList = MutableStateFlow<List<NoticePortfolio>>(listOf())
    val noticePortfolioList = _noticePortfolioList.asStateFlow()

    private var _nextTimerInMillis = MutableStateFlow<Long?>(null)
    val nextTimerInMillis = _nextTimerInMillis.asStateFlow()

    init {
        tickerFlow().launchIn(viewModelScope)
    }

    private fun tickerFlow() = flow {
        while (true) {
            emit(Unit)
            updateTimer()
            Log.d("@@@", ": delay ")
            delay(60000)
        }
    }

    fun getNoticePortfolioList() {
        viewModelScope.launch {
            _noticePortfolioList.value = noticePortfolioRepo.getAll()
            updateTimer()
        }
    }

    private fun updateTimer() {
        _nextTimerInMillis.value = _noticePortfolioList.value
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
            }
    }

    fun insertNoticePortfolio(noticePortfolio: NoticePortfolio) {
        viewModelScope.launch {
            noticePortfolioRepo.saveNotice(noticePortfolio)
            _noticePortfolioList.value = noticePortfolioRepo.getAll()
            updateTimer()
        }
    }

    fun updateNoticePortfolio(noticePortfolio: NoticePortfolio) {
        viewModelScope.launch {
            noticePortfolioRepo.updateNotice(noticePortfolio)
            _noticePortfolioList.value = noticePortfolioRepo.getAll()
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
            _noticePortfolioList.value = noticePortfolioRepo.getAll()
            updateTimer()
        }
    }
}