package com.aroman.mimwallet.domain.repository

import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.CoinDetails

interface CoinRepository {
    suspend fun getCoins(): List<DisplayableCoin>

    suspend fun getCoinDetailsBySymbol(symbol: String): HashMap<String, CoinDetails>
}