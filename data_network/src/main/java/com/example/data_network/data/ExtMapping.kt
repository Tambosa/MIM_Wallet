package com.example.data_network.data

import com.example.core_network.dto.coin_details_dto.CoinDetailsObjectDto
import com.example.core_network.dto.coin_dto.CoinDataDto
import com.example.data_network.domain.entity.CoinDetails
import com.example.data_network.domain.entity.DisplayableCoin

fun CoinDataDto.toCoin() = DisplayableCoin(
    id = id,
    name = name,
    symbol = symbol,
)

fun CoinDetailsObjectDto.toCoinDetails() = CoinDetails(
    id = id,
    name = name,
    symbol = symbol,
    price = quote.usd.price,
    percentChange1h = quote.usd.percentChange1h,
    percentChange24h = quote.usd.percentChange24h,
    percentChange7d = quote.usd.percentChange7d,
    percentChange30d = quote.usd.percentChange30d,
    percentChange60d = quote.usd.percentChange60d,
    percentChange90d = quote.usd.percentChange90d,
)