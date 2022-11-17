package com.aroman.mimwallet.data.repository

import android.content.Context
import com.aroman.mimwallet.data.remote.CoinMarketCapApi
import com.aroman.mimwallet.data.remote.dto.coin_details_dto.toCoinDetails
import com.aroman.mimwallet.data.remote.dto.coin_dto.toCoin
import com.aroman.mimwallet.domain.model.Coin
import com.aroman.mimwallet.domain.model.CoinDetails
import com.aroman.mimwallet.domain.repository.CoinRepository
import com.aroman.mimwallet.getApiKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinMarketCapApi,
    @ApplicationContext private val context: Context
) : CoinRepository {
    override suspend fun getCoins(): List<Coin> {
        return api.getCoins(context.getApiKey()).data.map { it.toCoin() }
    }

    override suspend fun getCoinDetailsBySymbol(symbol: String): HashMap<String, CoinDetails> {
        return hashMapOf<String, CoinDetails>(
            Pair(
                symbol,
                api.getCoinDetailBySymbol(context.getApiKey(), symbol).data[symbol]?.toCoinDetails()!!
            )
        )
    }

    override suspend fun getCoinDetailsByMultipleSymbols(vararg symbols: String): HashMap<String, CoinDetails> {
        val response = api.getCoinDetailBySymbol(context.getApiKey(), symbols.joinToString(","))
        val returnMap = hashMapOf<String, CoinDetails>()
        symbols.forEach { symbol ->
            if (response.data[symbol] != null) {
                returnMap[symbol] = response.data[symbol]!!.toCoinDetails()
            }
        }
        return returnMap
    }
}