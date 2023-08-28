package com.aroman.mimwallet.domain.model.ui

import com.aroman.mimwallet.presentation.ui.viewmodels.PortfolioViewModel.TimePeriod
import com.example.data_network.domain.entity.DisplayableCoin

data class PortfolioUiState(
    val coinList: List<DisplayableCoin>,
    val totalPrice: Double = 0.0,
    val totalPercentChange1h: Double = 0.0,
    val totalPercentChange24h: Double = 0.0,
    val totalPercentChange7d: Double = 0.0,
    val totalPercentChange30d: Double = 0.0,
    val totalPercentChange60d: Double = 0.0,
    val totalPercentChange90d: Double = 0.0,
    val timePeriod: TimePeriod = TimePeriod.TWENTY_FOUR_HOURS,
    val isLoading: Boolean = false,
    val isCache: Boolean,
    val isEditCountDialogShown: Boolean = false
)
