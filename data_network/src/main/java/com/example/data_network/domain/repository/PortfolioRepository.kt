package com.example.data_network.domain.repository

import com.example.data_network.domain.entity.DisplayableCoin

interface PortfolioRepository {
    suspend fun getAll(): List<DisplayableCoin>
    suspend fun saveCoin(coin: DisplayableCoin)
    suspend fun updateCoin(coin: DisplayableCoin)
    suspend fun deleteCoin(coin: DisplayableCoin)
}