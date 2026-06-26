package com.peak15.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.peak15.data.local.dao.*
import com.peak15.data.local.entities.*

@Database(
    entities = [
        DayProgressEntity::class,
        PelvicSessionEntity::class,
        CardioSessionEntity::class,
        WaterLogEntity::class,
        SleepLogEntity::class,
        DailyMetricsEntity::class,
        UserSettingsEntity::class,
        ConfidenceLogEntity::class,
        SupplementLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class Peak15Database : RoomDatabase() {

    abstract fun dayProgressDao(): DayProgressDao
    abstract fun pelvicSessionDao(): PelvicSessionDao
    abstract fun cardioSessionDao(): CardioSessionDao
    abstract fun waterLogDao(): WaterLogDao
    abstract fun sleepLogDao(): SleepLogDao
    abstract fun dailyMetricsDao(): DailyMetricsDao
    abstract fun userSettingsDao(): UserSettingsDao
    abstract fun confidenceLogDao(): ConfidenceLogDao
    abstract fun supplementLogDao(): SupplementLogDao

    companion object {
        const val DATABASE_NAME = "peak15.db"
    }
}
