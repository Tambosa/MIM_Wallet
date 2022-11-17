package com.aroman.mimwallet.domain.use_case.get_multiple_coin_details

import com.aroman.mimwallet.common.Resource
import com.aroman.mimwallet.domain.model.CoinDetails
import com.aroman.mimwallet.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetMultipleCoinDetailsUseCase @Inject constructor(private val repo: CoinRepository) {
    operator fun invoke(vararg coinSymbols: String): Flow<Resource<HashMap<String, CoinDetails>>> =
        flow {
            try {
                emit(Resource.Loading())
                val coin = repo.getCoinDetailsByMultipleSymbols(*coinSymbols)
                emit(Resource.Success(coin))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "Unknown Error"))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        e.message
                            ?: "GetMultipleCoinDetailsUseCase: Couldn't reach server. Check your internet connection"
                    )
                )
            }
        }
}