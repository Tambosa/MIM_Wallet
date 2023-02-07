package com.aroman.mimwallet.data.remote.dto.coin_dto

import com.google.gson.annotations.SerializedName

data class CoinDto(
    @SerializedName("data") val data: List<CoinDataDto>,
    @SerializedName("status") val status: CoinStatusDto
)