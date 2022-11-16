package com.aroman.mimwallet.domain.model

data class CoinDetails(
    val id: Int,
    val name: String,
    val symbol: String,
    val price: Double,
)