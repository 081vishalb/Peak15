package com.peak15.di

import android.content.Context
import androidx.room.Room
import com.peak15.data.local.dao.*
import com.peak15.data.local.database.Peak15Database
import com.peak15.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Peak15Database =
        Room.databaseBuilder(context, Peak15Database::class.java, Peak15Database.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideDayProgressDao(db: Peak15Database) = db.dayProgressDao()
    @Provides fun providePelvicSessionDao(db: Peak15Database) = db.pelvicSessionDao()
    @Provides fun provideCardioSessionDao(db: Peak15Database) = db.cardioSessionDao()
    @Provides fun provideWaterLogDao(db: Peak15Database) = db.waterLogDao()
    @Provides fun provideSleepLogDao(db: Peak15Database) = db.sleepLogDao()
    @Provides fun provideDailyMetricsDao(db: Peak15Database) = db.dailyMetricsDao()
    @Provides fun provideUserSettingsDao(db: Peak15Database) = db.userSettingsDao()
    @Provides fun provideConfidenceLogDao(db: Peak15Database) = db.confidenceLogDao()
    @Provides fun provideSupplementLogDao(db: Peak15Database) = db.supplementLogDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindProgramRepository(impl: ProgramRepositoryImpl): ProgramRepository

    @Binds @Singleton
    abstract fun bindProgressRepository(impl: ProgressRepositoryImpl): ProgressRepository

    @Binds @Singleton
    abstract fun bindWaterRepository(impl: WaterRepositoryImpl): WaterRepository

    @Binds @Singleton
    abstract fun bindSleepRepository(impl: SleepRepositoryImpl): SleepRepository

    @Binds @Singleton
    abstract fun bindPelvicRepository(impl: PelvicRepositoryImpl): PelvicRepository

    @Binds @Singleton
    abstract fun bindCardioRepository(impl: CardioRepositoryImpl): CardioRepository

    @Binds @Singleton
    abstract fun bindMetricsRepository(impl: MetricsRepositoryImpl): MetricsRepository

    @Binds @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
