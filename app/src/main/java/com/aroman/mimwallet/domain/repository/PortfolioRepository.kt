package com.aroman.mimwallet.domain.repository

import com.aroman.mimwallet.domain.model.DisplayableCoin

interface PortfolioRepository {
    suspend fun getAll(): List<DisplayableCoin>
    suspend fun saveCoin(coin: DisplayableCoin)
    suspend fun updateCoin(coin: DisplayableCoin)
    suspend fun deleteCoin(coin: DisplayableCoin)
}