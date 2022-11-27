package com.aroman.mimwallet.data.local.dao

import androidx.room.*
import com.aroman.mimwallet.data.local.tables.CoinRoomEntity
import com.aroman.mimwallet.data.local.tables.RoomConst

@Dao
interface CoinDao {
    @Query("SELECT * FROM ${RoomConst.TABLE_COIN}")
    fun getAllCoins(): List<CoinRoomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoin(coin: CoinRoomEntity)

    @Update
    fun updateCoin(coin: CoinRoomEntity)

    @Delete
    fun deleteCoin(coin: CoinRoomEntity)
}