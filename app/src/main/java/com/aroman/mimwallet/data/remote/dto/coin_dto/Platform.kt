package com.aroman.mimwallet.data.remote.dto.coin_dto

import com.google.gson.annotations.SerializedName

data class Platform(
    val id: Int,
    val name: String,
    val slug: String,
    val symbol: String,
    @SerializedName("token_address")
    val tokenAddress: String
)