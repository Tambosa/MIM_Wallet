package com.aroman.mimwallet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aroman.mimwallet.data.local.dao.CoinDao
import com.aroman.mimwallet.data.local.tables.CoinRoomEntity

@Database(
    entities = arrayOf(CoinRoomEntity::class),
    version = 1,
    exportSchema = false
)

abstract class RoomDb : RoomDatabase() {
    abstract fun coinDao(): CoinDao
}