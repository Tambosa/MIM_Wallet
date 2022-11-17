package com.aroman.mimwallet.domain.use_case.get_coins

import com.aroman.mimwallet.common.Resource
import com.aroman.mimwallet.domain.model.Coin
import com.aroman.mimwallet.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(private val repo: CoinRepository) {
    operator fun invoke(): Flow<Resource<List<Coin>>> = flow {
        try {
            emit(Resource.Loading())
            val coins = repo.getCoins()
            emit(Resource.Success(coins))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown Error"))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    e.message
                        ?: "GetCoinsUseCase: Couldn't reach server. Check your internet connection"
                )
            )
        }
    }
}