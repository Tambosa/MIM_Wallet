package com.aroman.mimwallet.data.remote.dto.coin_details_dto

import com.google.gson.annotations.SerializedName

data class CoinDetailsStatusDto(
    @SerializedName("credit_count") val creditCount: Int,
    @SerializedName("elapsed") val elapsed: Int,
    @SerializedName("error_code") val errorCode: Int,
    @SerializedName("error_message") val errorMessage: Any,
    @SerializedName("notice") val notice: Any,
    @SerializedName("timestamp") val timestamp: String
)