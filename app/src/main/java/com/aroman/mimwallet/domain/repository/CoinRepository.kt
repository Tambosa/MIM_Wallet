package com.aroman.mimwallet.domain.repository

import com.aroman.mimwallet.domain.model.Coin
import com.aroman.mimwallet.domain.model.CoinDetails

interface CoinRepository {
    suspend fun getCoins(): List<Coin>
    suspend fun getCoinDetailsBySymbol(symbol: String): CoinDetails?
}