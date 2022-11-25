package com.aroman.mimwallet.domain.use_case.get_coins

import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.model.Coin
import com.aroman.mimwallet.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(private val repo: CoinRepository) {
    operator fun invoke(): Flow<ViewState<List<Coin>>> = flow {
        try {
            emit(ViewState.Loading())
            val coins = repo.getCoins()
            emit(ViewState.Success(coins))
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