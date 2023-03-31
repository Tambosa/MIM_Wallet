package com.aroman.mimwallet.data.local.dao

import androidx.room.*
import com.aroman.mimwallet.data.local.RoomConst
import com.aroman.mimwallet.data.local.tables.UiPortfolioStateCoinListEntity
import com.aroman.mimwallet.data.local.tables.UiPortfolioStateEntity

@Dao
interface UiPortfolioStateDao {
    @Query("SELECT * FROM ${RoomConst.TABLE_PORTFOLIO_CACHE}")
    suspend fun getAll(): List<UiPortfolioStateEntity>

    @Query("SELECT * FROM ${RoomConst.TABLE_PORTFOLIO_CACHE} ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatest(): UiPortfolioStateEntity?

    @Query("SELECT * FROM ${RoomConst.TABLE_PORTFOLIO_CACHE_COIN_LIST} WHERE timestamp = :timestamp")
    suspend fun getPortfolioStateCoinList(timestamp: Long): List<UiPortfolioStateCoinListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(portfolioState: UiPortfolioStateEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePortfolioCoin(coin: UiPortfolioStateCoinListEntity)

    @Query("DELETE FROM ${RoomConst.TABLE_PORTFOLIO_CACHE}")
    suspend fun clearPortfolioCache()

    @Query("DELETE FROM ${RoomConst.TABLE_PORTFOLIO_CACHE_COIN_LIST}")
    suspend fun clearPortfolioCoinCache()
}