package com.aroman.mimwallet.data.local.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aroman.mimwallet.data.local.RoomConst
import com.aroman.mimwallet.domain.model.PortfolioUiState

@Entity(tableName = RoomConst.TABLE_PORTFOLIO_CACHE)
data class UiPortfolioStateEntity(
    @PrimaryKey(autoGenerate = false)
    val timestamp: Long,
    var totalPrice: Double,
    var totalPercentChange1h: Double,
    var totalPercentChange24h: Double,
    var totalPercentChange7d: Double,
    var totalPercentChange30d: Double,
    var totalPercentChange60d: Double,
    var totalPercentChange90d: Double,
)

fun UiPortfolioStateEntity.toPortfolioState(coinList: List<UiPortfolioStateCoinListEntity>) =
    PortfolioUiState(
        coinList = coinList.map { it.toDisplayableCoin() },
        totalPrice = totalPrice,
        totalPercentChange1h = totalPercentChange1h,
        totalPercentChange24h = totalPercentChange24h,
        totalPercentChange7d = totalPercentChange7d,
        totalPercentChange30d = totalPercentChange30d,
        totalPercentChange60d = totalPercentChange60d,
        totalPercentChange90d = totalPercentChange90d,
        isCache = false
    )