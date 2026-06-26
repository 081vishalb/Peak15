package com.peak15.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peak15.data.local.entities.*
import com.peak15.data.repository.*
import com.peak15.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// ─── Roadmap ViewModel ────────────────────────────────────────────────────────

data class RoadmapUiState(
    val days        : List<DayProgram>         = emptyList(),
    val progress    : Map<Int, DayProgressEntity> = emptyMap(),
    val currentDay  : Int                      = 1,
    val isLoading   : Boolean                  = true
)

@HiltViewModel
class RoadmapViewModel @Inject constructor(
    private val programRepo : ProgramRepository,
    private val progressRepo: ProgressRepository,
    private val settingsRepo: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoadmapUiState())
    val uiState: StateFlow<RoadmapUiState> = _uiState.asStateFlow()

    init {
        val days = programRepo.getAllDays()
        viewModelScope.launch {
            combine(
                progressRepo.getAllProgress(),
                settingsRepo.getSettings()
            ) { progressList, settings ->
                RoadmapUiState(
                    days       = days,
                    progress   = progressList.associateBy { it.day },
                    currentDay = settings?.currentDay ?: 1,
                    isLoading  = false
                )
            }.collect { _uiState.value = it }
        }
    }
}

// ─── Day Detail ViewModel ─────────────────────────────────────────────────────

data class DayDetailUiState(
    val program  : DayProgram?        = null,
    val progress : DayProgressEntity? = null,
    val isLoading: Boolean            = true
)

@HiltViewModel
class DayDetailViewModel @Inject constructor(
    private val programRepo : ProgramRepository,
    private val progressRepo: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DayDetailUiState())
    val uiState: StateFlow<DayDetailUiState> = _uiState.asStateFlow()

    fun loadDay(day: Int) {
        val program = programRepo.getDayProgram(day)
        viewModelScope.launch {
            progressRepo.getDayProgress(day).collect { progress ->
                _uiState.value = DayDetailUiState(
                    program   = program,
                    progress  = progress,
                    isLoading = false
                )
            }
        }
    }
}

// ─── Cardio ViewModel ─────────────────────────────────────────────────────────

data class CardioUiState(
    val currentDay     : Int                       = 1,
    val sessions       : List<CardioSessionEntity> = emptyList(),
    val timerRunning   : Boolean                   = false,
    val elapsedSeconds : Int                       = 0,
    val selectedType   : String                    = "WALK",
    val isLoading      : Boolean                   = true
)

@HiltViewModel
class CardioViewModel @Inject constructor(
    private val cardioRepo  : CardioRepository,
    private val progressRepo: ProgressRepository,
    private val settingsRepo: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardioUiState())
    val uiState: StateFlow<CardioUiState> = _uiState.asStateFlow()

    private var timerJob: kotlinx.coroutines.Job? = null

    init {
        viewModelScope.launch {
            settingsRepo.getSettings().collect { settings ->
                val day = settings?.currentDay ?: 1
                _uiState.update { it.copy(currentDay = day) }
                cardioRepo.getSessionsForDay(day).collect { sessions ->
                    _uiState.update { it.copy(sessions = sessions, isLoading = false) }
                }
            }
        }
    }

    fun selectType(type: String) {
        _uiState.update { it.copy(selectedType = type) }
    }

    fun startTimer() {
        _uiState.update { it.copy(timerRunning = true) }
        timerJob = viewModelScope.launch {
            while (_uiState.value.timerRunning) {
                kotlinx.coroutines.delay(1000L)
                _uiState.update { it.copy(elapsedSeconds = it.elapsedSeconds + 1) }
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(timerRunning = false) }
    }

    fun stopAndLog() {
        timerJob?.cancel()
        val state = _uiState.value
        val minutes = state.elapsedSeconds / 60
        if (minutes >= 1) {
            viewModelScope.launch {
                cardioRepo.logSession(
                    day            = state.currentDay,
                    type           = state.selectedType,
                    durationMinutes = minutes,
                    distanceKm     = 0f,
                    avgHR          = 0
                )
                progressRepo.updateCardioCompleted(state.currentDay, true)
            }
        }
        _uiState.update { it.copy(timerRunning = false, elapsedSeconds = 0) }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

// ─── Analytics ViewModel ─────────────────────────────────────────────────────

data class AnalyticsUiState(
    val allProgress  : List<DayProgressEntity>   = emptyList(),
    val allMetrics   : List<DailyMetricsEntity>  = emptyList(),
    val allSleep     : List<SleepLogEntity>       = emptyList(),
    val waterTotals  : List<com.peak15.data.local.dao.DailyWaterTotal> = emptyList(),
    val streak       : Int                        = 0,
    val avgSleep     : Float                      = 0f,
    val avgMood      : Float                      = 0f,
    val avgEQ        : Float                      = 0f,
    val totalWorkouts: Int                        = 0,
    val isLoading    : Boolean                    = true
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val progressRepo: ProgressRepository,
    private val metricsRepo : MetricsRepository,
    private val sleepRepo   : SleepRepository,
    private val waterRepo   : WaterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                progressRepo.getAllProgress(),
                metricsRepo.getAllMetrics(),
                sleepRepo.getAllSleepLogs(),
                waterRepo.getDailyTotals()
            ) { progress, metrics, sleep, water ->
                val streak       = progress.count { it.completionPercent >= 80 }
                val avgSleep     = if (sleep.isEmpty()) 0f else sleep.map { it.durationHours }.average().toFloat()
                val avgMood      = if (metrics.isEmpty()) 0f else metrics.map { it.moodScore }.average().toFloat()
                val avgEQ        = if (metrics.isEmpty()) 0f else metrics.map { it.erectionQualityScore }.average().toFloat()
                val totalWorkouts = progress.count { it.workoutCompleted }

                AnalyticsUiState(
                    allProgress   = progress,
                    allMetrics    = metrics,
                    allSleep      = sleep,
                    waterTotals   = water,
                    streak        = streak,
                    avgSleep      = avgSleep,
                    avgMood       = avgMood,
                    avgEQ         = avgEQ,
                    totalWorkouts = totalWorkouts,
                    isLoading     = false
                )
            }.collect { _uiState.value = it }
        }
    }
}

// ─── Settings ViewModel ───────────────────────────────────────────────────────

data class SettingsUiState(
    val settings : UserSettingsEntity? = null,
    val isDark   : Boolean             = false,
    val isLoading: Boolean             = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepo: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepo.getSettings().collect { settings ->
                _uiState.update { it.copy(settings = settings, isLoading = false) }
            }
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        val current = _uiState.value.settings ?: UserSettingsEntity()
        viewModelScope.launch {
            settingsRepo.upsertSettings(current.copy(notificationsEnabled = enabled))
        }
    }

    fun toggleWaterReminder(enabled: Boolean) {
        val current = _uiState.value.settings ?: UserSettingsEntity()
        viewModelScope.launch {
            settingsRepo.upsertSettings(current.copy(waterReminderEnabled = enabled))
        }
    }

    fun updatePornFreeStreak(streak: Int) {
        val current = _uiState.value.settings ?: UserSettingsEntity()
        viewModelScope.launch {
            settingsRepo.upsertSettings(current.copy(pornFreeStreak = streak))
        }
    }

    fun resetProgram() {
        val current = _uiState.value.settings ?: UserSettingsEntity()
        viewModelScope.launch {
            settingsRepo.upsertSettings(current.copy(currentDay = 1, programStartDate = 0L))
        }
    }

    fun startProgram() {
        viewModelScope.launch {
            settingsRepo.startProgram()
        }
    }
}
