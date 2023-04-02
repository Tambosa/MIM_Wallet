package com.aroman.mimwallet.domain.model

data class CoinDetails(
    val count: Double = 0.0,
    val id: Int,
    val name: String,
    val symbol: String,
    val price: Double,
    val percentChange1h: Double,
    val percentChange24h: Double,
    val percentChange7d: Double,
    val percentChange30d: Double,
    val percentChange60d: Double,
    val percentChange90d: Double,
)