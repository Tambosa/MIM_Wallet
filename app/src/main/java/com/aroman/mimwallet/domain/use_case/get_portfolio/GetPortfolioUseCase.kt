package com.aroman.mimwallet.domain.use_case.get_portfolio

import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.Portfolio
import com.aroman.mimwallet.domain.repository.CoinRepository
import com.aroman.mimwallet.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPortfolioUseCase @Inject constructor(
    private val remoteRepo: CoinRepository, private val localRepo: PortfolioRepository
) {
    operator fun invoke(): Flow<ViewState<Portfolio>> = flow {
        try {
            emit(ViewState.Loading())
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
                val portfolio = Portfolio(resultList)
                portfolio.apply {
                    totalPrice = calculateTotalPrice(portfolio)
                    totalPercentChange1h = calculatePercentDiffer(portfolio, "1h")
                    totalPercentChange24h = calculatePercentDiffer(portfolio, "24h")
                    totalPercentChange7d = calculatePercentDiffer(portfolio, "7d")
                    totalPercentChange30d = calculatePercentDiffer(portfolio, "30d")
                    totalPercentChange60d = calculatePercentDiffer(portfolio, "60d")
                    totalPercentChange90d = calculatePercentDiffer(portfolio, "90d")
                }
                emit(ViewState.Success(portfolio))
            } else emit(ViewState.Success(Portfolio(emptyList())))
        } catch (e: HttpException) {
            emit(ViewState.Error(e.localizedMessage ?: "Unknown Error"))
        } catch (e: IOException) {
            emit(
                ViewState.Error(
                    e.message
                        ?: "GetCoinsUseCase: Couldn't reach server. Check your internet connection"
                )
            )
        }
    }

    private fun calculateTotalPrice(portfolio: Portfolio): Double {
        var totalPrice = 0.0
        for (coin in portfolio.coinList) {
            totalPrice += coin.count * coin.price
        }
        return totalPrice
    }

    private fun calculatePercentDiffer(portfolio: Portfolio, s: String): Double {
        var oldTotalPrice = 0.0
        for (coin in portfolio.coinList) {
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
        return ((oldTotalPrice - portfolio.totalPrice) / portfolio.totalPrice) * 100
    }
}