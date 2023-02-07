package com.aroman.mimwallet.data.remote

import com.aroman.mimwallet.data.remote.dto.coin_details_dto.CoinDetailsDto
import com.aroman.mimwallet.data.remote.dto.coin_dto.CoinDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CoinMarketCapApi {

    @GET("/v1/cryptocurrency/map")
    suspend fun getCoins(
        @Header("X-CMC_PRO_API_KEY") apiKey: String
    ): CoinDto

    @GET("/v1/cryptocurrency/quotes/latest")
    suspend fun getCoinDetailBySymbol(
        @Header("X-CMC_PRO_API_KEY") apiKey: String,
        @Query("symbol") symbol: String
    ): CoinDetailsDto
}