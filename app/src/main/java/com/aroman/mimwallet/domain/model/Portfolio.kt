package com.aroman.mimwallet.domain.model

data class Portfolio(
    val coinList: List<DisplayableCoin>,
    var totalPrice: Double = 0.0,
    var totalPercentChange1h: Double = 0.0,
    var totalPercentChange24h: Double = 0.0,
    var totalPercentChange7d: Double = 0.0,
    var totalPercentChange30d: Double = 0.0,
    var totalPercentChange60d: Double = 0.0,
    var totalPercentChange90d: Double = 0.0,
)
