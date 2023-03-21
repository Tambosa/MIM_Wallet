package com.aroman.mimwallet.data.local.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aroman.mimwallet.data.local.RoomConst
import com.aroman.mimwallet.domain.model.DisplayableCoin

@Entity(tableName = RoomConst.TABLE_COIN)
data class CoinRoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val symbol: String,
    val count: Double,
)

fun CoinRoomEntity.toCoin() = DisplayableCoin(
    id = id,
    name = name,
    symbol = symbol,
    count = count
)