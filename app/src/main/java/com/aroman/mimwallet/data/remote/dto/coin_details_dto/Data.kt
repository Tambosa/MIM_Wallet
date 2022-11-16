package com.aroman.mimwallet.data.remote.dto.coin_details_dto

import com.aroman.mimwallet.domain.model.CoinDetails

data class Data(
    val `1`: X1
)

fun Data.toCoinDetails(): CoinDetails {
    return CoinDetails(
        id = `1`.id,
        name = `1`.name,
        symbol = `1`.symbol,
        price = `1`.quote.USD.price,
    )
}