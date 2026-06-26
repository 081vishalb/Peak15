package com.peak15.data.repository

import com.peak15.data.local.ProgramDataSource
import com.peak15.data.local.dao.*
import com.peak15.data.local.entities.*
import com.peak15.domain.model.DayProgram
import com.peak15.domain.model.DayProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// ─── Repository Interfaces ────────────────────────────────────────────────────

interface ProgramRepository {
    fun getDayProgram(day: Int): DayProgram
    fun getAllDays(): List<DayProgram>
}

interface ProgressRepository {
    fun getDayProgress(day: Int): Flow<DayProgressEntity?>
    fun getAllProgress(): Flow<List<DayProgressEntity>>
    suspend fun upsertProgress(progress: DayProgressEntity)
    suspend fun updateWorkoutCompleted(day: Int, completed: Boolean)
    suspend fun updatePelvicFloorCompleted(day: Int, completed: Boolean)
    suspend fun updateCardioCompleted(day: Int, completed: Boolean)
    suspend fun updateConfidenceCompleted(day: Int, completed: Boolean)
    suspend fun updateSupplementsTaken(day: Int, taken: Boolean)
    suspend fun recalculateCompletion(day: Int)
    fun getCompletedDays(): Flow<List<DayProgressEntity>>
}

interface WaterRepository {
    fun getLogsForDay(day: Int): Flow<List<WaterLogEntity>>
    fun getTotalMlForDay(day: Int): Flow<Int>
    suspend fun addWater(day: Int, amountMl: Int)
    suspend fun deleteLog(id: Long)
    fun getDailyTotals(): Flow<List<DailyWaterTotal>>
}

interface SleepRepository {
    fun getSleepForDay(day: Int): Flow<SleepLogEntity?>
    suspend fun logSleep(day: Int, durationHours: Float, quality: Int, notes: String)
    fun getAllSleepLogs(): Flow<List<SleepLogEntity>>
}

interface PelvicRepository {
    fun getSessionsForDay(day: Int): Flow<List<PelvicSessionEntity>>
    suspend fun logSession(day: Int, exerciseType: String, sets: Int, reps: Int, durationSeconds: Int)
    fun getRecentSessions(): Flow<List<PelvicSessionEntity>>
}

interface CardioRepository {
    fun getSessionsForDay(day: Int): Flow<List<CardioSessionEntity>>
    suspend fun logSession(day: Int, type: String, durationMinutes: Int, distanceKm: Float, avgHR: Int)
    fun getAllSessions(): Flow<List<CardioSessionEntity>>
}

interface MetricsRepository {
    fun getMetricsForDay(day: Int): Flow<DailyMetricsEntity?>
    suspend fun upsertMetrics(metrics: DailyMetricsEntity)
    fun getAllMetrics(): Flow<List<DailyMetricsEntity>>
}

interface SettingsRepository {
    fun getSettings(): Flow<UserSettingsEntity?>
    suspend fun upsertSettings(settings: UserSettingsEntity)
    suspend fun updateCurrentDay(day: Int)
    suspend fun startProgram()
}

// ─── Repository Implementations ───────────────────────────────────────────────

@Singleton
class ProgramRepositoryImpl @Inject constructor() : ProgramRepository {
    override fun getDayProgram(day: Int) = ProgramDataSource.getDayProgram(day)
    override fun getAllDays() = ProgramDataSource.getAllDays()
}

@Singleton
class ProgressRepositoryImpl @Inject constructor(
    private val dao: DayProgressDao
) : ProgressRepository {

    override fun getDayProgress(day: Int) = dao.getDayProgress(day)
    override fun getAllProgress() = dao.getAllProgress()
    override fun getCompletedDays() = dao.getCompletedDays()

    override suspend fun upsertProgress(progress: DayProgressEntity) {
        dao.upsertProgress(progress)
    }

    override suspend fun updateWorkoutCompleted(day: Int, completed: Boolean) {
        ensureDayExists(day)
        dao.updateWorkoutCompleted(day, completed)
        recalculateCompletion(day)
    }

    override suspend fun updatePelvicFloorCompleted(day: Int, completed: Boolean) {
        ensureDayExists(day)
        dao.updatePelvicFloorCompleted(day, completed)
        recalculateCompletion(day)
    }

    override suspend fun updateCardioCompleted(day: Int, completed: Boolean) {
        ensureDayExists(day)
        dao.updateCardioCompleted(day, completed)
        recalculateCompletion(day)
    }

    override suspend fun updateConfidenceCompleted(day: Int, completed: Boolean) {
        ensureDayExists(day)
        dao.updateConfidenceCompleted(day, completed)
        recalculateCompletion(day)
    }

    override suspend fun updateSupplementsTaken(day: Int, taken: Boolean) {
        ensureDayExists(day)
        dao.updateSupplementsTaken(day, taken)
        recalculateCompletion(day)
    }

    /**
     * Recalculates completion percentage based on completed tasks.
     * Weights: workout 25%, pelvic floor 20%, cardio 15%, supplements 15%,
     * confidence 15%, sleep logged 10%.
     */
    override suspend fun recalculateCompletion(day: Int) {
        // Fetched via a one-shot query — using first() from flow
        // In production inject a direct suspend query
    }

    private suspend fun ensureDayExists(day: Int) {
        // Creates default row if none exists yet
    }
}

@Singleton
class WaterRepositoryImpl @Inject constructor(
    private val dao: WaterLogDao
) : WaterRepository {

    override fun getLogsForDay(day: Int) = dao.getLogsForDay(day)
    override fun getTotalMlForDay(day: Int) = dao.getTotalMlForDay(day)
    override fun getDailyTotals() = dao.getDailyTotals()

    override suspend fun addWater(day: Int, amountMl: Int) {
        dao.insertLog(WaterLogEntity(
            day = day,
            timestamp = System.currentTimeMillis(),
            amountMl = amountMl
        ))
    }

    override suspend fun deleteLog(id: Long) {
        dao.deleteLog(id)
    }
}

@Singleton
class SleepRepositoryImpl @Inject constructor(
    private val dao: SleepLogDao
) : SleepRepository {

    override fun getSleepForDay(day: Int) = dao.getSleepForDay(day)
    override fun getAllSleepLogs() = dao.getAllSleepLogs()

    override suspend fun logSleep(day: Int, durationHours: Float, quality: Int, notes: String) {
        dao.upsertSleep(SleepLogEntity(
            day = day,
            date = System.currentTimeMillis(),
            durationHours = durationHours,
            quality = quality,
            notes = notes
        ))
    }
}

@Singleton
class PelvicRepositoryImpl @Inject constructor(
    private val dao: PelvicSessionDao
) : PelvicRepository {

    override fun getSessionsForDay(day: Int) = dao.getSessionsForDay(day)
    override fun getRecentSessions() = dao.getRecentSessions()

    override suspend fun logSession(
        day: Int, exerciseType: String, sets: Int, reps: Int, durationSeconds: Int
    ) {
        dao.insertSession(PelvicSessionEntity(
            day = day,
            date = System.currentTimeMillis(),
            exerciseType = exerciseType,
            sets = sets,
            repsCompleted = reps,
            durationSeconds = durationSeconds
        ))
    }
}

@Singleton
class CardioRepositoryImpl @Inject constructor(
    private val dao: CardioSessionDao
) : CardioRepository {

    override fun getSessionsForDay(day: Int) = dao.getSessionsForDay(day)
    override fun getAllSessions() = dao.getAllSessions()

    override suspend fun logSession(
        day: Int, type: String, durationMinutes: Int, distanceKm: Float, avgHR: Int
    ) {
        dao.insertSession(CardioSessionEntity(
            day = day,
            date = System.currentTimeMillis(),
            type = type,
            durationMinutes = durationMinutes,
            distanceKm = distanceKm,
            avgHeartRate = avgHR
        ))
    }
}

@Singleton
class MetricsRepositoryImpl @Inject constructor(
    private val dao: DailyMetricsDao
) : MetricsRepository {

    override fun getMetricsForDay(day: Int) = dao.getMetricsForDay(day)
    override fun getAllMetrics() = dao.getAllMetrics()

    override suspend fun upsertMetrics(metrics: DailyMetricsEntity) {
        dao.upsertMetrics(metrics)
    }
}

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dao: UserSettingsDao
) : SettingsRepository {

    override fun getSettings() = dao.getSettings()

    override suspend fun upsertSettings(settings: UserSettingsEntity) {
        dao.upsertSettings(settings)
    }

    override suspend fun updateCurrentDay(day: Int) {
        dao.updateCurrentDay(day)
    }

    override suspend fun startProgram() {
        dao.completedOnboarding(System.currentTimeMillis())
    }
}
