package com.peak15.domain.model

import kotlinx.serialization.Serializable

// ─── Day Program ────────────────────────────────────────────────────────────

data class DayProgram(
    val day: Int,                        // 1–15
    val phase: Phase,
    val title: String,
    val badge: String,
    val morningRoutine: String,
    val workout: WorkoutPlan,
    val pelvicFloor: PelvicFloorSession,
    val nutrition: NutritionPlan,
    val sleepTarget: Int,                // hours
    val mentalPerformance: String,
    val supplements: List<Supplement>,
    val thingsToAvoid: List<String>,
    val whyItWorks: String,
    val cardioTarget: CardioTarget
)

enum class Phase(val label: String, val description: String) {
    FOUNDATION("Foundation", "Days 1–5: Establish baselines"),
    BUILD("Build", "Days 6–10: Intensify training"),
    PEAK("Peak", "Days 11–15: Maximise performance")
}

// ─── Workout ─────────────────────────────────────────────────────────────────

data class WorkoutPlan(
    val name: String,
    val type: WorkoutType,
    val exercises: List<Exercise>,
    val totalDurationMinutes: Int,
    val notes: String
)

data class Exercise(
    val id: String,
    val name: String,
    val sets: Int,
    val reps: String,         // "3×15" or "3×max" or "45s"
    val restSeconds: Int,
    val instructions: String,
    val musclesWorked: List<String>,
    val category: ExerciseCategory
)

enum class WorkoutType { STRENGTH, HIIT, CARDIO, MOBILITY, ACTIVE_RECOVERY }
enum class ExerciseCategory { COMPOUND, ISOLATION, CORE, PELVIC, CARDIO, MOBILITY }

// ─── Pelvic Floor ─────────────────────────────────────────────────────────────

data class PelvicFloorSession(
    val totalMinutes: Int,
    val exercises: List<PelvicExercise>,
    val notes: String,
    val focusType: PelvicFocusType
)

data class PelvicExercise(
    val id: String,
    val name: String,
    val type: PelvicExerciseType,
    val contractSeconds: Int,
    val releaseSeconds: Int,
    val sets: Int,
    val repsPerSet: Int,
    val instructions: String
)

enum class PelvicExerciseType { KEGEL, REVERSE_KEGEL, QUICK_FLICK, ELEVATOR, BREATHING }
enum class PelvicFocusType { STRENGTH, RELEASE, CONTROL, INTEGRATION }

// ─── Cardio ──────────────────────────────────────────────────────────────────

data class CardioTarget(
    val type: CardioType,
    val durationMinutes: Int,
    val heartRateZone: HeartRateZone,
    val notes: String
)

enum class CardioType { WALK, RUN, CYCLE, SWIMMING, HIIT_SPRINT }
enum class HeartRateZone(val label: String, val minPercent: Int, val maxPercent: Int) {
    ZONE2("Aerobic Base", 60, 70),
    ZONE3("Tempo", 71, 80),
    ZONE4("Threshold", 81, 90),
    ZONE5("Max", 91, 100)
}

// ─── Nutrition ───────────────────────────────────────────────────────────────

data class NutritionPlan(
    val totalCalories: Int,
    val proteinGrams: Int,
    val carbGrams: Int,
    val fatGrams: Int,
    val meals: List<Meal>,
    val waterTargetLiters: Float,
    val specialFoods: List<String>
)

data class Meal(
    val id: String,
    val name: String,               // "Breakfast", "Lunch", etc.
    val time: String,               // "8:00 AM"
    val foods: List<String>,
    val proteinGrams: Int,
    val notes: String
)

// ─── Supplements ─────────────────────────────────────────────────────────────

data class Supplement(
    val name: String,
    val dosage: String,
    val timing: String,
    val purpose: String,
    val evidenceLevel: EvidenceLevel
)

enum class EvidenceLevel { STRONG, MODERATE, EMERGING }

// ─── Progress & Tracking ─────────────────────────────────────────────────────

data class DayProgress(
    val day: Int,
    val date: Long,
    val workoutCompleted: Boolean,
    val pelvicFloorCompleted: Boolean,
    val cardioCompleted: Boolean,
    val waterIntakeLiters: Float,
    val sleepHours: Float,
    val mealsCompleted: Int,
    val supplementsTaken: Boolean,
    val confidenceChallengeCompleted: Boolean,
    val recoveryCompleted: Boolean,
    val moodScore: Int,              // 1–10
    val energyScore: Int,            // 1–10
    val erectionQualityScore: Int,   // 1–10 (morning erection quality)
    val notes: String,
    val completionPercent: Int       // calculated
)

// ─── Confidence Training ──────────────────────────────────────────────────────

data class ConfidenceChallenge(
    val day: Int,
    val title: String,
    val description: String,
    val category: ConfidenceCategory,
    val durationMinutes: Int,
    val instructions: List<String>
)

enum class ConfidenceCategory { SOCIAL, POSTURE, BREATHWORK, VISUALIZATION, AFFIRMATION, EYE_CONTACT }

// ─── Recovery ────────────────────────────────────────────────────────────────

data class RecoveryRoutine(
    val name: String,
    val durationMinutes: Int,
    val exercises: List<StretchExercise>
)

data class StretchExercise(
    val name: String,
    val holdSeconds: Int,
    val sets: Int,
    val instructions: String,
    val targetArea: String
)
