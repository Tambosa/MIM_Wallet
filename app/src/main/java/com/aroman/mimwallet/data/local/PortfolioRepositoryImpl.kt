package com.aroman.mimwallet.data.local

import com.aroman.mimwallet.data.local.dao.CoinDao
import com.aroman.mimwallet.data.local.tables.CoinRoomEntity
import com.aroman.mimwallet.data.local.tables.toCoin
import com.example.data_network.domain.entity.DisplayableCoin
import com.example.data_network.domain.repository.PortfolioRepository

class PortfolioRepositoryImpl(private val dao: CoinDao) : PortfolioRepository {
    override suspend fun getAll(): List<DisplayableCoin> = dao.getAllCoins().map { it.toCoin() }

    override suspend fun saveCoin(coin: DisplayableCoin) {
        dao.insertCoin(CoinRoomEntity(coin.id, coin.name, coin.symbol, coin.count))
    }

    override suspend fun updateCoin(coin: DisplayableCoin) {
        dao.updateCoin(CoinRoomEntity(coin.id, coin.name, coin.symbol, coin.count))
    }

    override suspend fun deleteCoin(coin: DisplayableCoin) {
        dao.deleteCoin(CoinRoomEntity(coin.id, coin.name, coin.symbol, coin.count))
    }
}