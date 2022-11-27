package com.aroman.mimwallet.domain.model

data class Coin(
    val id: Int,
    val name: String,
    val symbol: String,
    val count: Double = 0.0
) : DisplayableItem