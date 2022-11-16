package com.aroman.mimwallet.data.remote.dto.coin_dto

import com.aroman.mimwallet.domain.model.Coin
import com.google.gson.annotations.SerializedName

data class Data(
    val first_historical_data: String,
    val id: Int,
    @SerializedName("is_active")
    val isActive: Int,
    @SerializedName("last_historical_data")
    val lastHistoricalData: String,
    val name: String,
    val platform: Platform,
    val rank: Int,
    val slug: String,
    val symbol: String
)

fun Data.toCoin(): Coin {
    return Coin(
        id = id,
        name = name,
        symbol = symbol,
    )
}