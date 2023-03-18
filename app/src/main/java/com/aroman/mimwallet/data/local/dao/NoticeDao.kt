package com.aroman.mimwallet.data.local.dao

import androidx.room.*
import com.aroman.mimwallet.data.local.tables.NoticePortfolioRoomEntity
import com.aroman.mimwallet.data.local.RoomConst

@Dao
interface NoticeDao {
    @Query("SELECT * FROM ${RoomConst.TABLE_NOTICE}")
    suspend fun getAllNotice(): List<NoticePortfolioRoomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotice(notice: NoticePortfolioRoomEntity)

    @Update
    suspend fun updateNotice(notice: NoticePortfolioRoomEntity)

    @Delete
    suspend fun deleteNotice(notice: NoticePortfolioRoomEntity)
}