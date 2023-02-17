package com.aroman.mimwallet.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.aroman.mimwallet.R
import com.aroman.mimwallet.common.Constants
import com.aroman.mimwallet.data.local.PortfolioRepositoryImpl
import com.aroman.mimwallet.data.local.RoomDb
import com.aroman.mimwallet.data.local.dao.CoinDao
import com.aroman.mimwallet.data.local.tables.RoomConst
import com.aroman.mimwallet.data.remote.CoinMarketCapApi
import com.aroman.mimwallet.data.repository.CoinRepositoryImpl
import com.aroman.mimwallet.domain.repository.CoinRepository
import com.aroman.mimwallet.domain.repository.PortfolioRepository
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

    @Provides
    @Singleton
    fun provideRoomDb(@ApplicationContext context: Context): RoomDb {
        return Room.databaseBuilder(
            context,
            RoomDb::class.java,
            RoomConst.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCoinDao(db: RoomDb): CoinDao {
        return db.coinDao()
    }

    @Provides
    @Singleton
    fun providePortfolioRepository(
        dao: CoinDao
    ): PortfolioRepository {
        return PortfolioRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(context.getString((R.string.app_name)), Context.MODE_PRIVATE)
    }
}