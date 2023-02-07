package com.aroman.mimwallet.data.remote.dto.coin_dto

import com.google.gson.annotations.SerializedName

data class CoinPlatformDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("token_address") val tokenAddress: String
)