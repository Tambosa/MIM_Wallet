package com.example.data_network.domain.repository

import com.example.data_network.domain.entity.CoinDetails
import com.example.data_network.domain.entity.DisplayableCoin

interface CoinRepository {
    suspend fun getCoins(): List<DisplayableCoin>

    suspend fun getCoinDetailsBySymbol(symbol: String): HashMap<String, CoinDetails>
}