package com.aroman.mimwallet.domain.repository

import com.aroman.mimwallet.domain.model.Coin

interface PortfolioRepository {
    suspend fun getAll(): List<Coin>
    suspend fun saveCoin(coin: Coin)
    suspend fun updateCoin(coin: Coin)
    suspend fun deleteCoin(coin: Coin)
}