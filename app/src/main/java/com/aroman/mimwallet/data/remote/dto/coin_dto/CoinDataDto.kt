package com.aroman.mimwallet.data.remote.dto.coin_dto

import com.aroman.mimwallet.domain.model.Coin
import com.google.gson.annotations.SerializedName

data class CoinDataDto(
    @SerializedName("first_historical_data") val firstHistoricalData: String,
    @SerializedName("id") val id: Int,
    @SerializedName("is_active") val isActive: Int,
    @SerializedName("last_historical_data") val lastHistoricalData: String,
    @SerializedName("name") val name: String,
    @SerializedName("platform") val platform: CoinPlatformDto,
    @SerializedName("rank") val rank: Int,
    @SerializedName("slug") val slug: String,
    @SerializedName("symbol") val symbol: String
)

fun CoinDataDto.toCoin() = Coin(
    id = id,
    name = name,
    symbol = symbol,
)