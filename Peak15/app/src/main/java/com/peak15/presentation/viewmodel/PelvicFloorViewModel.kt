package com.peak15.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peak15.data.local.entities.PelvicSessionEntity
import com.peak15.data.repository.PelvicRepository
import com.peak15.data.repository.ProgressRepository
import com.peak15.data.repository.SettingsRepository
import com.peak15.domain.model.PelvicExercise
import com.peak15.domain.model.PelvicExerciseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// ─── Timer Phase ─────────────────────────────────────────────────────────────

enum class TimerPhase {
    IDLE,
    CONTRACT,   // Kegel hold
    RELEASE,    // Reverse Kegel / rest
    REST,       // Between sets
    COMPLETE
}

// ─── Pelvic Floor UI State ────────────────────────────────────────────────────

data class PelvicFloorUiState(
    val currentDay         : Int                    = 1,
    val exercises          : List<PelvicExercise>   = emptyList(),
    val currentExerciseIdx : Int                    = 0,
    val currentSet         : Int                    = 1,
    val currentRep         : Int                    = 1,
    val timerPhase         : TimerPhase             = TimerPhase.IDLE,
    val secondsRemaining   : Int                    = 0,
    val totalSeconds       : Int                    = 0,
    val isRunning          : Boolean                = false,
    val sessionCompleted   : Boolean                = false,
    val recentSessions     : List<PelvicSessionEntity> = emptyList(),
    val todaySessionCount  : Int                    = 0
) {
    val currentExercise: PelvicExercise?
        get() = exercises.getOrNull(currentExerciseIdx)

    val progressFraction: Float
        get() = if (totalSeconds > 0) 1f - (secondsRemaining.toFloat() / totalSeconds) else 0f

    val phaseLabel: String
        get() = when (timerPhase) {
            TimerPhase.CONTRACT -> "CONTRACT"
            TimerPhase.RELEASE  -> "RELEASE"
            TimerPhase.REST     -> "REST"
            TimerPhase.IDLE     -> "READY"
            TimerPhase.COMPLETE -> "DONE"
        }

    val phaseInstruction: String
        get() = when (timerPhase) {
            TimerPhase.CONTRACT -> "Squeeze and lift your pelvic floor"
            TimerPhase.RELEASE  -> "Fully let go — open and release"
            TimerPhase.REST     -> "Rest between sets. Breathe."
            TimerPhase.IDLE     -> currentExercise?.instructions ?: ""
            TimerPhase.COMPLETE -> "Session complete!"
        }
}

// ─── ViewModel ────────────────────────────────────────────────────────────────

@HiltViewModel
class PelvicFloorViewModel @Inject constructor(
    private val pelvicRepo  : PelvicRepository,
    private val progressRepo: ProgressRepository,
    private val settingsRepo: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PelvicFloorUiState())
    val uiState: StateFlow<PelvicFloorUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var sessionStartTime = 0L

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            settingsRepo.getSettings().collect { settings ->
                val day = settings?.currentDay ?: 1
                _uiState.update { it.copy(currentDay = day) }

                combine(
                    pelvicRepo.getSessionsForDay(day),
                    pelvicRepo.getRecentSessions()
                ) { todaySessions, recent ->
                    _uiState.update { state ->
                        state.copy(
                            todaySessionCount = todaySessions.size,
                            recentSessions    = recent
                        )
                    }
                }.collect()
            }
        }
    }

    /** Load exercises for a specific day's program */
    fun loadExercises(exercises: List<PelvicExercise>) {
        _uiState.update { it.copy(
            exercises          = exercises,
            currentExerciseIdx = 0,
            currentSet         = 1,
            currentRep         = 1,
            timerPhase         = TimerPhase.IDLE,
            sessionCompleted   = false
        )}
    }

    /** Start / resume the timer */
    fun startTimer() {
        val state = _uiState.value
        val exercise = state.currentExercise ?: return
        sessionStartTime = System.currentTimeMillis()

        when (state.timerPhase) {
            TimerPhase.IDLE, TimerPhase.COMPLETE -> beginContractPhase(exercise)
            else -> resumeTimer()
        }
    }

    private fun beginContractPhase(exercise: PelvicExercise) {
        val duration = when (exercise.type) {
            PelvicExerciseType.QUICK_FLICK -> 1
            PelvicExerciseType.BREATHING   -> exercise.releaseSeconds
            else -> exercise.contractSeconds
        }
        val phase = when (exercise.type) {
            PelvicExerciseType.REVERSE_KEGEL, PelvicExerciseType.BREATHING -> TimerPhase.RELEASE
            else -> TimerPhase.CONTRACT
        }
        startCountdown(duration, phase)
    }

    private fun resumeTimer() {
        _uiState.update { it.copy(isRunning = true) }
        runCountdown()
    }

    private fun startCountdown(seconds: Int, phase: TimerPhase) {
        _uiState.update { it.copy(
            timerPhase       = phase,
            secondsRemaining = seconds,
            totalSeconds     = seconds,
            isRunning        = true
        )}
        runCountdown()
    }

    private fun runCountdown() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.secondsRemaining > 0 && _uiState.value.isRunning) {
                delay(1000L)
                _uiState.update { it.copy(secondsRemaining = it.secondsRemaining - 1) }
            }
            if (_uiState.value.isRunning) {
                onPhaseComplete()
            }
        }
    }

    private fun onPhaseComplete() {
        val state    = _uiState.value
        val exercise = state.currentExercise ?: return

        when (state.timerPhase) {
            TimerPhase.CONTRACT -> {
                // Move to release phase
                startCountdown(exercise.releaseSeconds, TimerPhase.RELEASE)
            }
            TimerPhase.RELEASE -> {
                // Check if more reps in this set
                if (state.currentRep < exercise.repsPerSet) {
                    _uiState.update { it.copy(currentRep = it.currentRep + 1) }
                    beginContractPhase(exercise)
                } else {
                    // Set complete — rest or next set
                    if (state.currentSet < exercise.sets) {
                        _uiState.update { it.copy(
                            currentSet = it.currentSet + 1,
                            currentRep = 1
                        )}
                        startCountdown(10, TimerPhase.REST) // 10s rest between sets
                    } else {
                        // Exercise complete — next exercise or finish
                        moveToNextExercise()
                    }
                }
            }
            TimerPhase.REST -> {
                beginContractPhase(exercise)
            }
            else -> {}
        }
    }

    private fun moveToNextExercise() {
        val state = _uiState.value
        if (state.currentExerciseIdx < state.exercises.size - 1) {
            _uiState.update { it.copy(
                currentExerciseIdx = it.currentExerciseIdx + 1,
                currentSet         = 1,
                currentRep         = 1,
                timerPhase         = TimerPhase.IDLE,
                isRunning          = false
            )}
        } else {
            completeSession()
        }
    }

    private fun completeSession() {
        _uiState.update { it.copy(
            timerPhase       = TimerPhase.COMPLETE,
            isRunning        = false,
            sessionCompleted = true
        )}
        val day = _uiState.value.currentDay
        val duration = ((System.currentTimeMillis() - sessionStartTime) / 1000).toInt()
        viewModelScope.launch {
            pelvicRepo.logSession(
                day           = day,
                exerciseType  = _uiState.value.currentExercise?.type?.name ?: "KEGEL",
                sets          = _uiState.value.currentExercise?.sets ?: 3,
                reps          = _uiState.value.currentExercise?.repsPerSet ?: 10,
                durationSeconds = duration
            )
            progressRepo.updatePelvicFloorCompleted(day, true)
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isRunning = false) }
    }

    fun resetTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(
            currentExerciseIdx = 0,
            currentSet         = 1,
            currentRep         = 1,
            timerPhase         = TimerPhase.IDLE,
            secondsRemaining   = 0,
            totalSeconds       = 0,
            isRunning          = false,
            sessionCompleted   = false
        )}
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
