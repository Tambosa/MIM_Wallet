package com.example.core_network.dto.coin_details_dto

import com.google.gson.annotations.SerializedName

data class CoinDetailsQuoteDto(
    @SerializedName("USD") val usd: CoinDetailsUsdDto
)