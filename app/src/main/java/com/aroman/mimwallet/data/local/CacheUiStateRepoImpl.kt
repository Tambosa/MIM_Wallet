package com.aroman.mimwallet.data.local

import com.aroman.mimwallet.data.local.dao.UiPortfolioStateDao
import com.aroman.mimwallet.data.local.tables.UiPortfolioStateCoinListEntity
import com.aroman.mimwallet.data.local.tables.UiPortfolioStateEntity
import com.aroman.mimwallet.data.local.tables.toPortfolioState
import com.aroman.mimwallet.domain.model.ui.PortfolioUiState
import com.aroman.mimwallet.domain.repository.CacheUiStateRepo

class CacheUiStateRepoImpl(private val dao: UiPortfolioStateDao) : CacheUiStateRepo {
    override suspend fun savePortfolioState(portfolio: PortfolioUiState) {
        dao.clearPortfolioCache()
        dao.clearPortfolioCoinCache()
        val timestamp = System.currentTimeMillis()
        dao.save(
            UiPortfolioStateEntity(
                timestamp = timestamp,
                totalPrice = portfolio.totalPrice,
                totalPercentChange1h = portfolio.totalPercentChange1h,
                totalPercentChange24h = portfolio.totalPercentChange24h,
                totalPercentChange7d = portfolio.totalPercentChange7d,
                totalPercentChange30d = portfolio.totalPercentChange30d,
                totalPercentChange60d = portfolio.totalPercentChange60d,
                totalPercentChange90d = portfolio.totalPercentChange90d,
            )
        )
        portfolio.coinList.forEach { coin ->
            dao.savePortfolioCoin(
                UiPortfolioStateCoinListEntity(
                    timestamp = timestamp,
                    id = coin.id,
                    name = coin.name,
                    symbol = coin.symbol,
                    count = coin.count,
                    price = coin.price,
                    percentChange1h = coin.percentChange1h,
                    percentChange24h = coin.percentChange24h,
                    percentChange7d = coin.percentChange7d,
                    percentChange30d = coin.percentChange30d,
                    percentChange60d = coin.percentChange60d,
                    percentChange90d = coin.percentChange90d,
                )
            )
        }
    }

    override suspend fun getPortfolioState(): PortfolioUiState? {
        val portfolioState = dao.getLatest()
        var coinList: List<UiPortfolioStateCoinListEntity> = emptyList()
        portfolioState?.let { state ->
            state.timestamp.let { timestamp ->
                coinList = dao.getPortfolioStateCoinList(timestamp)
            }
        }
        return portfolioState?.toPortfolioState(coinList)
    }
}