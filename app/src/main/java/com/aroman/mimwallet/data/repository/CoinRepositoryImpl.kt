package com.aroman.mimwallet.data.repository

import com.aroman.mimwallet.data.remote.CoinMarketCapApi
import com.aroman.mimwallet.data.remote.dto.coin_details_dto.CoinDetailsDto
import com.aroman.mimwallet.data.remote.dto.coin_dto.CoinDto
import com.aroman.mimwallet.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(private val api: CoinMarketCapApi) : CoinRepository {
    override suspend fun getCoins(): CoinDto {
        return api.getCoins()
    }

    override suspend fun getCoinDetailsBySymbol(symbol: String): CoinDetailsDto {
        return api.getCoinDetailBySymbol(symbol)
    }
}