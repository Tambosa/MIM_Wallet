package com.aroman.mimwallet.presentation_compose.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.model.Portfolio
import com.aroman.mimwallet.domain.use_case.get_portfolio.GetPortfolioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ComposeNotificationViewModel @Inject constructor(
    private val getPortfolioUseCase: GetPortfolioUseCase
) : ViewModel() {
    private val _portfolio = MutableStateFlow<ViewState<Portfolio>>(ViewState.Loading())
    val portfolio = _portfolio.asStateFlow()

    private val _shouldShowNotification = MutableStateFlow(false)
    val shouldShowNotification = _shouldShowNotification.asStateFlow()

    fun requestNotification() {
        getPortfolioUseCase().onEach { result ->
            _portfolio.value = result
            _shouldShowNotification.value = true
        }.launchIn(viewModelScope)
    }

    fun notificationFinished() {
        _shouldShowNotification.value = false
    }
}