package com.peak15.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Stores the user's daily progress snapshot.
 * One row per day (day 1–15).
 */
@Entity(tableName = "day_progress")
data class DayProgressEntity(
    @PrimaryKey val day: Int,
    val date: Long = 0L,
    val workoutCompleted: Boolean = false,
    val pelvicFloorCompleted: Boolean = false,
    val cardioCompleted: Boolean = false,
    val waterIntakeLiters: Float = 0f,
    val sleepHours: Float = 0f,
    val mealsCompleted: Int = 0,
    val supplementsTaken: Boolean = false,
    val confidenceChallengeCompleted: Boolean = false,
    val recoveryCompleted: Boolean = false,
    val moodScore: Int = 0,
    val energyScore: Int = 0,
    val erectionQualityScore: Int = 0,
    val notes: String = "",
    val completionPercent: Int = 0
)

/**
 * Tracks individual pelvic floor training sessions.
 * Multiple sessions can be logged per day.
 */
@Entity(tableName = "pelvic_sessions")
data class PelvicSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val day: Int,
    val date: Long,
    val exerciseType: String,        // "KEGEL" | "REVERSE_KEGEL" | "QUICK_FLICK" etc.
    val sets: Int,
    val repsCompleted: Int,
    val durationSeconds: Int,
    val notes: String = ""
)

/**
 * Tracks cardio sessions.
 */
@Entity(tableName = "cardio_sessions")
data class CardioSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val day: Int,
    val date: Long,
    val type: String,                // "WALK" | "RUN" | "CYCLE" etc.
    val durationMinutes: Int,
    val distanceKm: Float = 0f,
    val avgHeartRate: Int = 0,
    val caloriesBurned: Int = 0,
    val notes: String = ""
)

/**
 * Water intake log — supports multiple entries per day.
 */
@Entity(tableName = "water_log")
data class WaterLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val day: Int,
    val timestamp: Long,
    val amountMl: Int                // in millilitres
)

/**
 * Sleep log — one entry per day.
 */
@Entity(tableName = "sleep_log")
data class SleepLogEntity(
    @PrimaryKey val day: Int,
    val date: Long,
    val bedtimeTimestamp: Long = 0L,
    val wakeTimestamp: Long = 0L,
    val durationHours: Float,
    val quality: Int,                // 1–10 self-reported
    val notes: String = ""
)

/**
 * Daily metrics for analytics — mood, energy, erection quality.
 */
@Entity(tableName = "daily_metrics")
data class DailyMetricsEntity(
    @PrimaryKey val day: Int,
    val date: Long,
    val moodScore: Int = 5,
    val energyScore: Int = 5,
    val erectionQualityScore: Int = 5,
    val stressLevel: Int = 5,
    val libidoScore: Int = 5,
    val focusScore: Int = 5
)

/**
 * Supplement taken log.
 */
@Entity(tableName = "supplement_log")
data class SupplementLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val day: Int,
    val timestamp: Long,
    val supplementName: String,
    val taken: Boolean = true
)

/**
 * User settings and onboarding state.
 */
@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val programStartDate: Long = 0L,
    val currentDay: Int = 1,
    val notificationsEnabled: Boolean = true,
    val morningNotificationHour: Int = 7,
    val morningNotificationMinute: Int = 0,
    val eveningNotificationHour: Int = 21,
    val eveningNotificationMinute: Int = 0,
    val waterReminderEnabled: Boolean = true,
    val waterReminderIntervalHours: Int = 2,
    val weightKg: Float = 0f,
    val heightCm: Float = 0f,
    val targetSleepHours: Float = 8f,
    val pornFreeStreak: Int = 0,
    val onboardingCompleted: Boolean = false
)

/**
 * Confidence challenge completions.
 */
@Entity(tableName = "confidence_log")
data class ConfidenceLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val day: Int,
    val date: Long,
    val category: String,
    val challengeTitle: String,
    val completed: Boolean = true,
    val reflection: String = ""
)
