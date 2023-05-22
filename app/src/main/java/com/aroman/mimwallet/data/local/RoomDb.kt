package com.aroman.mimwallet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aroman.mimwallet.data.local.dao.CoinDao
import com.aroman.mimwallet.data.local.dao.NoticeDao
import com.aroman.mimwallet.data.local.dao.UiPortfolioStateDao
import com.aroman.mimwallet.data.local.tables.CoinRoomEntity
import com.aroman.mimwallet.data.local.tables.NoticePortfolioRoomEntity
import com.aroman.mimwallet.data.local.tables.UiPortfolioStateCoinListEntity
import com.aroman.mimwallet.data.local.tables.UiPortfolioStateEntity

@Database(
    entities = [CoinRoomEntity::class, NoticePortfolioRoomEntity::class, UiPortfolioStateEntity::class, UiPortfolioStateCoinListEntity::class],
    version = 1,
    exportSchema = false,
)

abstract class RoomDb : RoomDatabase() {
    abstract fun coinDao(): CoinDao
    abstract fun noticeDao(): NoticeDao
    abstract fun uiPortfolioStateDao(): UiPortfolioStateDao
}