package com.example.data_network.data

import com.example.core_network.CoinMarketCapApi
import com.example.data_network.BuildConfig
import com.example.data_network.domain.entity.CoinDetails
import com.example.data_network.domain.entity.DisplayableCoin
import com.example.data_network.domain.repository.CoinRepository
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