package com.aroman.mimwallet.data.remote.dto.coin_details_dto

import com.aroman.mimwallet.domain.model.CoinDetails
import com.google.gson.annotations.SerializedName

data class CoinDetailsObjectDto(
    @SerializedName("circulating_supply") val circulatingSupply: Double,
    @SerializedName("cmc_rank") val cmcRank: Int,
    @SerializedName("date_added") val dateAdded: String,
    @SerializedName("id") val id: Int,
    @SerializedName("is_active") val isActive: Int,
    @SerializedName("is_fiat") val isFiat: Int,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("max_supply") val maxSupply: Int,
    @SerializedName("name") val name: String,
    @SerializedName("num_market_pairs") val numMarketPairs: Int,
    @SerializedName("platform") val platform: Any,
    @SerializedName("quote") val quote: CoinDetailsQuoteDto,
    @SerializedName("self_reported_circulating_supply") val selfReportedCirculatingSupply: Any,
    @SerializedName("self_reported_market_cap") val selfReportedMarketCap: Any,
    @SerializedName("slug") val slug: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("total_supply") val totalSupply: Int,
    @SerializedName("tvl_ratio") val tvlRatio: Any
)

fun CoinDetailsObjectDto.toCoinDetails() = CoinDetails(
    id = id,
    name = name,
    symbol = symbol,
    price = quote.usd.price,
)