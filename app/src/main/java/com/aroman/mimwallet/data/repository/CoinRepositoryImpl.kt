package com.aroman.mimwallet.data.repository

import android.content.Context
import com.aroman.mimwallet.data.remote.CoinMarketCapApi
import com.aroman.mimwallet.data.remote.dto.coin_details_dto.toCoinDetails
import com.aroman.mimwallet.data.remote.dto.coin_dto.toCoin
import com.aroman.mimwallet.domain.model.CoinDetails
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.repository.CoinRepository
import com.aroman.mimwallet.getApiKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinMarketCapApi,
    @ApplicationContext private val context: Context
) : CoinRepository {
    override suspend fun getCoins(): List<DisplayableCoin> {
        return api.getCoins(context.getApiKey()).data.map { it.toCoin() }
    }

    override suspend fun getCoinDetailsBySymbol(symbol: String): HashMap<String, CoinDetails> {
        val resultDto = api.getCoinDetailBySymbol(context.getApiKey(), symbol).data
        val result = HashMap<String, CoinDetails>()
        for (mutableEntry in resultDto) {
            result[mutableEntry.key] = mutableEntry.value.toCoinDetails()
        }
        return result
    }
}