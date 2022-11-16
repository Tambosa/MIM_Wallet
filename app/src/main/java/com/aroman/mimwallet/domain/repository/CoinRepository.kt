package com.aroman.mimwallet.domain.repository

import com.aroman.mimwallet.data.remote.dto.coin_details_dto.CoinDetailsDto
import com.aroman.mimwallet.data.remote.dto.coin_dto.CoinDto

interface CoinRepository {
    suspend fun getCoins(): CoinDto
    suspend fun getCoinDetailsBySymbol(symbol: String): CoinDetailsDto
}