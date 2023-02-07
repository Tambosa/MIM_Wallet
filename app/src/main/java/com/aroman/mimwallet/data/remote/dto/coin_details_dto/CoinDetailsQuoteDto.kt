package com.aroman.mimwallet.data.remote.dto.coin_details_dto

import com.google.gson.annotations.SerializedName

data class CoinDetailsQuoteDto(
    @SerializedName("USD") val usd: CoinDetailsUsdDto
)