package com.aroman.mimwallet.data.local.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aroman.mimwallet.data.local.RoomConst
import com.example.data_network.domain.entity.DisplayableCoin

@Entity(tableName = RoomConst.TABLE_PORTFOLIO_CACHE_COIN_LIST)
data class UiPortfolioStateCoinListEntity(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int = 0,
    val timestamp: Long,
    val id: Int,
    val name: String,
    val symbol: String,
    var count: Double,
    val price: Double,
    val percentChange1h: Double,
    val percentChange24h: Double,
    val percentChange7d: Double,
    val percentChange30d: Double,
    val percentChange60d: Double,
    val percentChange90d: Double,
)

fun UiPortfolioStateCoinListEntity.toDisplayableCoin() = DisplayableCoin(
    id = id,
    name = name,
    symbol = symbol,
    count = count,
    price = price,
    percentChange1h = percentChange1h,
    percentChange24h = percentChange24h,
    percentChange7d = percentChange7d,
    percentChange30d = percentChange30d,
    percentChange60d = percentChange60d,
    percentChange90d = percentChange90d,
)