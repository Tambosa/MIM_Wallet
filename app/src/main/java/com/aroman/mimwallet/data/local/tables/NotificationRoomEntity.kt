package com.aroman.mimwallet.data.local.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aroman.mimwallet.data.local.RoomConst
import com.aroman.mimwallet.domain.model.NoticePortfolio

@Entity(tableName = RoomConst.TABLE_NOTICE)
data class NoticePortfolioRoomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val hour: Int,
    val minute: Int,
    val isActive: Boolean
)

fun NoticePortfolioRoomEntity.toNoticePortfolio() = NoticePortfolio(
    id = id,
    hour = hour,
    minute = minute,
    isActive = isActive,
)