package com.aroman.mimwallet.data.repository

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

    override suspend fun getCoinDetailsBySymbol(symbol: String): HashMap<String, CoinDetails> {
        return hashMapOf<String, CoinDetails>(
            Pair(
                symbol,
                api.getCoinDetailBySymbol(symbol).data[symbol]?.toCoinDetails()!!
            )
        )
    }

    override suspend fun getCoinDetailsByMultipleSymbols(vararg symbols: String): HashMap<String, CoinDetails> {
        val response = api.getCoinDetailBySymbol(symbols.joinToString(","))
        val returnMap = hashMapOf<String, CoinDetails>()
        symbols.forEach { symbol ->
            if (response.data[symbol] != null) {
                returnMap[symbol] = response.data[symbol]!!.toCoinDetails()
            }
        }
        return returnMap
    }
}