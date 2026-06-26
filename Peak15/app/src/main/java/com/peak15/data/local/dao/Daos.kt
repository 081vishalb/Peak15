package com.peak15.data.local.dao

import androidx.room.*
import com.peak15.data.local.entities.*
import kotlinx.coroutines.flow.Flow

// ─── Day Progress DAO ─────────────────────────────────────────────────────────

@Dao
interface DayProgressDao {

    @Query("SELECT * FROM day_progress WHERE day = :day")
    fun getDayProgress(day: Int): Flow<DayProgressEntity?>

    @Query("SELECT * FROM day_progress ORDER BY day ASC")
    fun getAllProgress(): Flow<List<DayProgressEntity>>

    @Query("SELECT * FROM day_progress WHERE workoutCompleted = 1 AND pelvicFloorCompleted = 1 AND cardioCompleted = 1")
    fun getCompletedDays(): Flow<List<DayProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: DayProgressEntity)

    @Query("UPDATE day_progress SET workoutCompleted = :completed WHERE day = :day")
    suspend fun updateWorkoutCompleted(day: Int, completed: Boolean)

    @Query("UPDATE day_progress SET pelvicFloorCompleted = :completed WHERE day = :day")
    suspend fun updatePelvicFloorCompleted(day: Int, completed: Boolean)

    @Query("UPDATE day_progress SET cardioCompleted = :completed WHERE day = :day")
    suspend fun updateCardioCompleted(day: Int, completed: Boolean)

    @Query("UPDATE day_progress SET supplementsTaken = :taken WHERE day = :day")
    suspend fun updateSupplementsTaken(day: Int, taken: Boolean)

    @Query("UPDATE day_progress SET confidenceChallengeCompleted = :completed WHERE day = :day")
    suspend fun updateConfidenceCompleted(day: Int, completed: Boolean)

    @Query("UPDATE day_progress SET completionPercent = :percent WHERE day = :day")
    suspend fun updateCompletionPercent(day: Int, percent: Int)

    @Query("SELECT COUNT(*) FROM day_progress WHERE completionPercent >= 80")
    suspend fun getStreakCount(): Int
}

// ─── Pelvic Floor DAO ─────────────────────────────────────────────────────────

@Dao
interface PelvicSessionDao {

    @Query("SELECT * FROM pelvic_sessions WHERE day = :day ORDER BY date DESC")
    fun getSessionsForDay(day: Int): Flow<List<PelvicSessionEntity>>

    @Query("SELECT COUNT(*) FROM pelvic_sessions WHERE day = :day")
    suspend fun getSessionCountForDay(day: Int): Int

    @Insert
    suspend fun insertSession(session: PelvicSessionEntity)

    @Query("SELECT SUM(durationSeconds) FROM pelvic_sessions WHERE day = :day")
    suspend fun getTotalDurationForDay(day: Int): Int?

    @Query("SELECT * FROM pelvic_sessions ORDER BY date DESC LIMIT 50")
    fun getRecentSessions(): Flow<List<PelvicSessionEntity>>
}

// ─── Cardio DAO ───────────────────────────────────────────────────────────────

@Dao
interface CardioSessionDao {

    @Query("SELECT * FROM cardio_sessions WHERE day = :day")
    fun getSessionsForDay(day: Int): Flow<List<CardioSessionEntity>>

    @Insert
    suspend fun insertSession(session: CardioSessionEntity)

    @Query("SELECT SUM(durationMinutes) FROM cardio_sessions WHERE day = :day")
    suspend fun getTotalMinutesForDay(day: Int): Int?

    @Query("SELECT * FROM cardio_sessions ORDER BY date DESC")
    fun getAllSessions(): Flow<List<CardioSessionEntity>>
}

// ─── Water Log DAO ────────────────────────────────────────────────────────────

@Dao
interface WaterLogDao {

    @Query("SELECT * FROM water_log WHERE day = :day ORDER BY timestamp DESC")
    fun getLogsForDay(day: Int): Flow<List<WaterLogEntity>>

    @Query("SELECT COALESCE(SUM(amountMl), 0) FROM water_log WHERE day = :day")
    fun getTotalMlForDay(day: Int): Flow<Int>

    @Insert
    suspend fun insertLog(log: WaterLogEntity)

    @Query("DELETE FROM water_log WHERE id = :id")
    suspend fun deleteLog(id: Long)

    @Query("SELECT day, COALESCE(SUM(amountMl), 0) as totalMl FROM water_log GROUP BY day ORDER BY day ASC")
    fun getDailyTotals(): Flow<List<DailyWaterTotal>>
}

data class DailyWaterTotal(val day: Int, val totalMl: Int)

// ─── Sleep Log DAO ────────────────────────────────────────────────────────────

@Dao
interface SleepLogDao {

    @Query("SELECT * FROM sleep_log WHERE day = :day")
    fun getSleepForDay(day: Int): Flow<SleepLogEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSleep(sleep: SleepLogEntity)

    @Query("SELECT * FROM sleep_log ORDER BY day ASC")
    fun getAllSleepLogs(): Flow<List<SleepLogEntity>>

    @Query("SELECT AVG(durationHours) FROM sleep_log")
    suspend fun getAverageSleepHours(): Float?
}

// ─── Daily Metrics DAO ───────────────────────────────────────────────────────

@Dao
interface DailyMetricsDao {

    @Query("SELECT * FROM daily_metrics WHERE day = :day")
    fun getMetricsForDay(day: Int): Flow<DailyMetricsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMetrics(metrics: DailyMetricsEntity)

    @Query("SELECT * FROM daily_metrics ORDER BY day ASC")
    fun getAllMetrics(): Flow<List<DailyMetricsEntity>>

    @Query("SELECT AVG(erectionQualityScore) FROM daily_metrics")
    suspend fun getAverageErectionQuality(): Float?

    @Query("SELECT AVG(moodScore) FROM daily_metrics")
    suspend fun getAverageMood(): Float?
}

// ─── User Settings DAO ────────────────────────────────────────────────────────

@Dao
interface UserSettingsDao {

    @Query("SELECT * FROM user_settings WHERE id = 1")
    fun getSettings(): Flow<UserSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSettings(settings: UserSettingsEntity)

    @Query("UPDATE user_settings SET currentDay = :day WHERE id = 1")
    suspend fun updateCurrentDay(day: Int)

    @Query("UPDATE user_settings SET pornFreeStreak = :streak WHERE id = 1")
    suspend fun updatePornFreeStreak(streak: Int)

    @Query("UPDATE user_settings SET programStartDate = :date, onboardingCompleted = 1 WHERE id = 1")
    suspend fun completedOnboarding(date: Long)
}

// ─── Confidence Log DAO ──────────────────────────────────────────────────────

@Dao
interface ConfidenceLogDao {

    @Query("SELECT * FROM confidence_log WHERE day = :day")
    fun getLogForDay(day: Int): Flow<List<ConfidenceLogEntity>>

    @Insert
    suspend fun insertLog(log: ConfidenceLogEntity)

    @Query("SELECT COUNT(*) FROM confidence_log WHERE completed = 1")
    suspend fun getTotalCompletedChallenges(): Int
}

// ─── Supplement Log DAO ──────────────────────────────────────────────────────

@Dao
interface SupplementLogDao {

    @Query("SELECT * FROM supplement_log WHERE day = :day ORDER BY timestamp DESC")
    fun getLogsForDay(day: Int): Flow<List<SupplementLogEntity>>

    @Insert
    suspend fun insertLog(log: SupplementLogEntity)
}
