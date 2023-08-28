package com.aroman.mimwallet.domain.model.ui

import com.aroman.mimwallet.presentation.ui.viewmodels.PortfolioViewModel.TimePeriod
import com.example.data_network.domain.entity.DisplayableCoin

sealed class PortfolioUiEvent {
    object ShowData : PortfolioUiEvent()
    data class UpdateCoin(val coin: DisplayableCoin) : PortfolioUiEvent()
    data class DeleteCoin(val coin: DisplayableCoin) : PortfolioUiEvent()
    data class ChangeTimePeriod(val timePeriod: TimePeriod) : PortfolioUiEvent()

    data class ShowDialog(val isVisible: Boolean) : PortfolioUiEvent()
}