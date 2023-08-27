package com.aroman.mimwallet.data.remote

import com.aroman.mimwallet.BuildConfig
import com.aroman.mimwallet.domain.model.CoinDetails
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.repository.CoinRepository
import com.example.core_network.CoinMarketCapApi
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinMarketCapApi
) : CoinRepository {
    override suspend fun getCoins(): List<DisplayableCoin> {
        return api.getCoins(BuildConfig.CMC_API_KEY).data.map { it.toCoin() }
    }

    override suspend fun getCoinDetailsBySymbol(symbol: String): HashMap<String, CoinDetails> {
        val resultDto = api.getCoinDetailBySymbol(BuildConfig.CMC_API_KEY, symbol).data
        val result = HashMap<String, CoinDetails>()
        for (mutableEntry in resultDto) {
            result[mutableEntry.key] = mutableEntry.value.toCoinDetails()
        }
        return result
    }
}