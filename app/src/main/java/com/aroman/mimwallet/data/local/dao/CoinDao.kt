package com.aroman.mimwallet.data.local.dao

import androidx.room.*
import com.aroman.mimwallet.data.local.tables.CoinRoomEntity
import com.aroman.mimwallet.data.local.RoomConst

@Dao
interface CoinDao {
    @Query("SELECT * FROM ${RoomConst.TABLE_COIN}")
    suspend fun getAllCoins(): List<CoinRoomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoin(coin: CoinRoomEntity)

    @Update
    suspend fun updateCoin(coin: CoinRoomEntity)

    @Delete
    suspend fun deleteCoin(coin: CoinRoomEntity)
}