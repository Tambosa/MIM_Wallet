package com.aroman.mimwallet.data.local

import com.aroman.mimwallet.data.local.dao.CoinDao
import com.aroman.mimwallet.data.local.tables.CoinRoomEntity
import com.aroman.mimwallet.data.local.tables.toCoin
import com.aroman.mimwallet.domain.model.Coin
import com.aroman.mimwallet.domain.repository.PortfolioRepository

class PortfolioRepositoryImpl(private val dao: CoinDao) : PortfolioRepository {
    override suspend fun getAll(): List<Coin> = dao.getAllCoins().map { it.toCoin() }

    override suspend fun saveCoin(coin: Coin) {
        dao.insertCoin(CoinRoomEntity(coin.id, coin.name, coin.symbol, coin.count))
    }

    override suspend fun updateCoin(coin: Coin) {
        dao.updateCoin(CoinRoomEntity(coin.id, coin.name, coin.symbol, coin.count))
    }

    override suspend fun deleteCoin(coin: Coin) {
        dao.deleteCoin(CoinRoomEntity(coin.id, coin.name, coin.symbol, coin.count))
    }
}