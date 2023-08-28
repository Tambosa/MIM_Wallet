package com.aroman.mimwallet.domain.use_case.get_portfolio

import com.aroman.mimwallet.domain.model.ui.PortfolioUiState
import com.aroman.mimwallet.domain.repository.CacheUiStateRepo
import com.example.data_network.domain.entity.DisplayableCoin
import com.example.data_network.domain.repository.CoinRepository
import com.example.data_network.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPortfolioUseCase @Inject constructor(
    private val remoteRepo: CoinRepository,
    private val localRepo: PortfolioRepository,
    private val cacheRepo: CacheUiStateRepo,
) {
    private var isColdStart: Boolean = true
    operator fun invoke(): Flow<PortfolioUiState> = flow {
        try {
            if (isColdStart) {
                val cachedPortfolio = cacheRepo.getPortfolioState()
                if (cachedPortfolio != null) {
                    emit(cachedPortfolio.copy(isCache = true))
                }
                isColdStart = false
            }
            val localCoins = localRepo.getAll()
            if (localCoins.isNotEmpty()) {
                val coinSymbols =
                    localCoins.joinToString(separator = ",", transform = { it.symbol })
                val coinDetailsList = remoteRepo.getCoinDetailsBySymbol(coinSymbols)
                val resultList = mutableListOf<DisplayableCoin>()
                for (localCoin in localCoins) {
                    resultList.add(
                        DisplayableCoin(
                            id = localCoin.id,
                            name = localCoin.name,
                            symbol = localCoin.symbol,
                            count = localCoin.count,
                            price = coinDetailsList[localCoin.symbol]!!.price,
                            percentChange1h = coinDetailsList[localCoin.symbol]!!.percentChange1h,
                            percentChange24h = coinDetailsList[localCoin.symbol]!!.percentChange24h,
                            percentChange7d = coinDetailsList[localCoin.symbol]!!.percentChange7d,
                            percentChange30d = coinDetailsList[localCoin.symbol]!!.percentChange30d,
                            percentChange60d = coinDetailsList[localCoin.symbol]!!.percentChange60d,
                            percentChange90d = coinDetailsList[localCoin.symbol]!!.percentChange90d,
                        )
                    )
                }
                val portfolio = PortfolioUiState(
                    resultList, isCache = false,
                    totalPrice = calculateTotalPrice(resultList),
                    totalPercentChange1h = calculatePercentDiffer(resultList, "1h"),
                    totalPercentChange24h = calculatePercentDiffer(resultList, "24h"),
                    totalPercentChange7d = calculatePercentDiffer(resultList, "7d"),
                    totalPercentChange30d = calculatePercentDiffer(resultList, "30d"),
                    totalPercentChange60d = calculatePercentDiffer(resultList, "60d"),
                    totalPercentChange90d = calculatePercentDiffer(resultList, "90d"),
                )
                cacheRepo.savePortfolioState(portfolio)
                emit(portfolio)
            } else emit(PortfolioUiState(emptyList(), isCache = false))
        } catch (e: Throwable) {
            //nothing
        }
    }

    private fun calculateTotalPrice(coinList: List<DisplayableCoin>): Double {
        var totalPrice = 0.0
        for (coin in coinList) {
            totalPrice += coin.count * coin.price
        }
        return totalPrice
    }

    private fun calculatePercentDiffer(resultList: List<DisplayableCoin>, s: String): Double {
        var oldTotalPrice = 0.0
        for (coin in resultList) {
            val differ = (coin.count * coin.price * (when (s) {
                "1h" -> coin.percentChange1h
                "7d" -> coin.percentChange7d
                "30d" -> coin.percentChange30d
                "60d" -> coin.percentChange60d
                "90d" -> coin.percentChange90d
                else -> coin.percentChange24h
            } / 100))
            oldTotalPrice += ((coin.count * coin.price) + differ)
        }
        return ((oldTotalPrice - calculateTotalPrice(resultList)) / calculateTotalPrice(resultList)) * 100
    }
}