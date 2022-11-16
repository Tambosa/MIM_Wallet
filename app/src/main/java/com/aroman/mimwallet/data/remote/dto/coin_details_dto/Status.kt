package com.aroman.mimwallet.data.remote.dto.coin_details_dto

data class Status(
    val credit_count: Int,
    val elapsed: Int,
    val error_code: Int,
    val error_message: String,
    val timestamp: String
)