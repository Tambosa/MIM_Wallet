package com.aroman.mimwallet.di

import android.content.Context
import com.aroman.mimwallet.common.Constants
import com.aroman.mimwallet.data.remote.CoinMarketCapApi
import com.aroman.mimwallet.data.repository.CoinRepositoryImpl
import com.aroman.mimwallet.domain.repository.CoinRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCoinMarketCapApi(): CoinMarketCapApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.COIN_MARKET_CAP_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinMarketCapApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinRepository(
        api: CoinMarketCapApi,
        @ApplicationContext context: Context
    ): CoinRepository {
        return CoinRepositoryImpl(api, context)
    }
}