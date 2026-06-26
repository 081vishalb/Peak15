package com.peak15.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peak15.data.local.dao.DailyWaterTotal
import com.peak15.data.local.entities.*
import com.peak15.data.repository.*
import com.peak15.domain.model.DayProgram
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// ─── Dashboard UI State ───────────────────────────────────────────────────────

data class DashboardUiState(
    val currentDay        : Int                 = 1,
    val dayProgram        : DayProgram?         = null,
    val progress          : DayProgressEntity?  = null,
    val waterTodayMl      : Int                 = 0,
    val waterTargetMl     : Int                 = 3500,
    val sleepLog          : SleepLogEntity?     = null,
    val metrics           : DailyMetricsEntity? = null,
    val streak            : Int                 = 0,
    val overallCompletion : Float               = 0f,
    val isLoading         : Boolean             = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val programRepo  : ProgramRepository,
    private val progressRepo : ProgressRepository,
    private val waterRepo    : WaterRepository,
    private val sleepRepo    : SleepRepository,
    private val metricsRepo  : MetricsRepository,
    private val settingsRepo : SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        observeDashboard()
    }

    private fun observeDashboard() {
        viewModelScope.launch {
            settingsRepo.getSettings().collect { settings ->
                val day = settings?.currentDay ?: 1

                combine(
                    progressRepo.getDayProgress(day),
                    waterRepo.getTotalMlForDay(day),
                    sleepRepo.getSleepForDay(day),
                    metricsRepo.getMetricsForDay(day),
                    progressRepo.getAllProgress()
                ) { progress, water, sleep, metrics, allProgress ->

                    val program  = programRepo.getDayProgram(day)
                    val streak   = allProgress.count { it.completionPercent >= 80 }
                    val overall  = if (allProgress.isEmpty()) 0f
                                   else allProgress.map { it.completionPercent }.average().toFloat() / 100f

                    DashboardUiState(
                        currentDay        = day,
                        dayProgram        = program,
                        progress          = progress,
                        waterTodayMl      = water,
                        waterTargetMl     = (program.nutrition.waterTargetLiters * 1000).toInt(),
                        sleepLog          = sleep,
                        metrics           = metrics,
                        streak            = streak,
                        overallCompletion = overall,
                        isLoading         = false
                    )
                }.collect { state ->
                    _uiState.value = state
                }
            }
        }
    }

    fun addWater(amountMl: Int) {
        viewModelScope.launch {
            waterRepo.addWater(_uiState.value.currentDay, amountMl)
        }
    }

    fun toggleWorkout() {
        val day = _uiState.value.currentDay
        val current = _uiState.value.progress?.workoutCompleted ?: false
        viewModelScope.launch {
            progressRepo.updateWorkoutCompleted(day, !current)
        }
    }

    fun togglePelvicFloor() {
        val day = _uiState.value.currentDay
        val current = _uiState.value.progress?.pelvicFloorCompleted ?: false
        viewModelScope.launch {
            progressRepo.updatePelvicFloorCompleted(day, !current)
        }
    }

    fun toggleCardio() {
        val day = _uiState.value.currentDay
        val current = _uiState.value.progress?.cardioCompleted ?: false
        viewModelScope.launch {
            progressRepo.updateCardioCompleted(day, !current)
        }
    }

    fun toggleSupplements() {
        val day = _uiState.value.currentDay
        val current = _uiState.value.progress?.supplementsTaken ?: false
        viewModelScope.launch {
            progressRepo.updateSupplementsTaken(day, !current)
        }
    }

    fun logSleep(hours: Float, quality: Int) {
        viewModelScope.launch {
            sleepRepo.logSleep(_uiState.value.currentDay, hours, quality, "")
        }
    }

    fun saveMetrics(mood: Int, energy: Int, erectionQuality: Int) {
        viewModelScope.launch {
            metricsRepo.upsertMetrics(
                DailyMetricsEntity(
                    day                 = _uiState.value.currentDay,
                    date                = System.currentTimeMillis(),
                    moodScore           = mood,
                    energyScore         = energy,
                    erectionQualityScore= erectionQuality
                )
            )
        }
    }

    fun advanceDay() {
        val next = (_uiState.value.currentDay + 1).coerceAtMost(15)
        viewModelScope.launch {
            settingsRepo.updateCurrentDay(next)
        }
    }
}
