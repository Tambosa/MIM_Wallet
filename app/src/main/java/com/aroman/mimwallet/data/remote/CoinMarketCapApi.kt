package com.aroman.mimwallet.data.remote

import com.aroman.mimwallet.data.remote.dto.coin_details_dto.CoinDetailsDto
import com.aroman.mimwallet.data.remote.dto.coin_dto.CoinDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CoinMarketCapApi {

    @Headers("X-CMC_PRO_API_KEY: " + "78d4a68b-15ac-43ef-9736-02dca921e4c1")
    @GET("/v1/cryptocurrency/map")
    suspend fun getCoins(): CoinDto

    @Headers("X-CMC_PRO_API_KEY: " + "78d4a68b-15ac-43ef-9736-02dca921e4c1")
    @GET("/v1/cryptocurrency/quotes/latest")
    suspend fun getCoinDetailBySymbol(@Query("symbol") symbol: String): CoinDetailsDto
}