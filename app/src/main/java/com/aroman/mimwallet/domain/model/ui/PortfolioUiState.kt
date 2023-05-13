package com.aroman.mimwallet.domain.model.ui

import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.presentation.ui.viewmodels.PortfolioViewModel.TimePeriod

data class PortfolioUiState(
    val coinList: List<DisplayableCoin>,
    var totalPrice: Double = 0.0,
    var totalPercentChange1h: Double = 0.0,
    var totalPercentChange24h: Double = 0.0,
    var totalPercentChange7d: Double = 0.0,
    var totalPercentChange30d: Double = 0.0,
    var totalPercentChange60d: Double = 0.0,
    var totalPercentChange90d: Double = 0.0,
    var timePeriod: TimePeriod = TimePeriod.TWENTY_FOUR_HOURS,
    var isLoading: Boolean = false,
    var isCache: Boolean
)
