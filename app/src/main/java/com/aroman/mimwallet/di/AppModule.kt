package com.aroman.mimwallet.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.aroman.mimwallet.R
import com.aroman.mimwallet.data.local.*
import com.aroman.mimwallet.data.local.dao.CoinDao
import com.aroman.mimwallet.data.local.dao.NoticeDao
import com.aroman.mimwallet.data.local.dao.UiPortfolioStateDao
import com.aroman.mimwallet.domain.repository.CacheUiStateRepo
import com.aroman.mimwallet.domain.repository.NoticePortfolioRepository
import com.example.core_network.CoinMarketCapApi
import com.example.core_network.Constants
import com.example.data_network.data.CoinRepositoryImpl
import com.example.data_network.domain.repository.CoinRepository
import com.example.data_network.domain.repository.PortfolioRepository
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
    fun provideCoinRepository(api: CoinMarketCapApi): CoinRepository {
        return CoinRepositoryImpl(api)
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
    fun provideNoticeDao(db: RoomDb): NoticeDao {
        return db.noticeDao()
    }

    @Provides
    @Singleton
    fun provideNoticeRepository(
        dao: NoticeDao
    ): NoticePortfolioRepository {
        return NoticePortfolioRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideUiPortfolioStateDao(db: RoomDb): UiPortfolioStateDao {
        return db.uiPortfolioStateDao()
    }

    @Provides
    @Singleton
    fun provideUiPortfolioStateRepository(
        dao: UiPortfolioStateDao
    ): CacheUiStateRepo {
        return CacheUiStateRepoImpl(dao)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString((R.string.app_name)),
            Context.MODE_PRIVATE
        )
    }
}