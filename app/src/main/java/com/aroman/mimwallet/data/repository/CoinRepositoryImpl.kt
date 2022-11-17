package com.aroman.mimwallet.data.repository

import android.util.Log
import com.aroman.mimwallet.data.remote.CoinMarketCapApi
import com.aroman.mimwallet.data.remote.dto.coin_details_dto.toCoinDetails
import com.aroman.mimwallet.data.remote.dto.coin_dto.toCoin
import com.aroman.mimwallet.domain.model.Coin
import com.aroman.mimwallet.domain.model.CoinDetails
import com.aroman.mimwallet.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(private val api: CoinMarketCapApi) : CoinRepository {
    override suspend fun getCoins(): List<Coin> {
        return api.getCoins().data.map { it.toCoin() }
    }

    override suspend fun getCoinDetailsBySymbol(symbol: String): CoinDetails? {
        return api.getCoinDetailBySymbol(symbol).data[symbol]?.toCoinDetails()
    }
}