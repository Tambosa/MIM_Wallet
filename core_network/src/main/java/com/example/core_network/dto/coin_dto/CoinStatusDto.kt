package com.example.core_network.dto.coin_dto

import com.google.gson.annotations.SerializedName

data class CoinStatusDto(
    @SerializedName("credit_count") val creditCount: Int,
    @SerializedName("elapsed") val elapsed: Int,
    @SerializedName("error_code") val errorCode: Int,
    @SerializedName("error_message") val errorMessage: String,
    @SerializedName("timestamp") val timestamp: String
)