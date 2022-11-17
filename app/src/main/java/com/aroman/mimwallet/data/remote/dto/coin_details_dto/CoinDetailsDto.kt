package com.aroman.mimwallet.data.remote.dto.coin_details_dto

import com.google.gson.annotations.SerializedName

data class CoinDetailsDto(
    @SerializedName("data") val data: HashMap<String, CoinDetailsObjectDto>,
    @SerializedName("status") val status: CoinDetailsStatusDto
)