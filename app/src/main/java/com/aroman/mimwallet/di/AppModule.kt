package com.aroman.mimwallet.di

import com.aroman.mimwallet.common.Constants
import com.aroman.mimwallet.data.remote.CoinMarketCapApi
import com.aroman.mimwallet.data.repository.CoinRepositoryImpl
import com.aroman.mimwallet.domain.repository.CoinRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCoinMarketCapApi(): CoinMarketCapApi {
        return Retrofit.Builder()
            .baseUrl(Constants.COIN_MARKET_CAP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinMarketCapApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinRepository(api: CoinMarketCapApi): CoinRepository {
        return CoinRepositoryImpl(api)
    }
}