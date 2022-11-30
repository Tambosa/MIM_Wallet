package com.aroman.mimwallet.domain.use_case.get_portfolio

import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.model.DisplayableCoin
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
    operator fun invoke(): Flow<ViewState<List<DisplayableCoin>>> = flow {
        try {
            emit(ViewState.Loading())
            val localCoins = localRepo.getAll()
            val coinSymbols = localCoins.joinToString(separator = ",", transform = { it.symbol })
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
            emit(ViewState.Success(resultList))
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
}