package com.peak15.presentation.screens.pelvicfloor

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peak15.domain.model.*
import com.peak15.presentation.components.*
import com.peak15.presentation.theme.*
import com.peak15.presentation.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PelvicFloorScreen(
    onBack: () -> Unit,
    vm    : PelvicFloorViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    // Load current day's exercises
    LaunchedEffect(state.currentDay) {
        // Default exercise set for standalone access
        val defaultExercises = listOf(
            PelvicExercise("def_1", "Foundation Kegel", PelvicExerciseType.KEGEL,
                5, 5, 3, 10, "Contract for 5 seconds, release fully for 5 seconds."),
            PelvicExercise("def_2", "Reverse Kegel", PelvicExerciseType.REVERSE_KEGEL,
                0, 8, 3, 8, "Deep belly breath — let pelvic floor gently descend and open."),
            PelvicExercise("def_3", "Quick Flicks", PelvicExerciseType.QUICK_FLICK,
                1, 1, 3, 10, "Rapid contract and full release — 10 in a row."),
            PelvicExercise("def_4", "Elevator Kegel", PelvicExerciseType.ELEVATOR,
                8, 8, 3, 5, "Contract 25%, 50%, 75%, 100%, then release in stages.")
        )
        vm.loadExercises(defaultExercises)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pelvic Floor Trainer",
                    style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (state.todaySessionCount > 0) {
                        Surface(
                            shape = CircleShape,
                            color = Peak15Colors.Secondary.copy(alpha = 0.15f)
                        ) {
                            Text(
                                "${state.todaySessionCount} today",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Peak15Colors.Secondary
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(Peak15Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Peak15Spacing.md)
        ) {

            // ── Timer section ─────────────────────────────────────────────────
            item {
                TimerCard(
                    state  = state,
                    onStart= { vm.startTimer() },
                    onPause= { vm.pauseTimer() },
                    onReset= { vm.resetTimer() }
                )
            }

            // ── Current exercise info ─────────────────────────────────────────
            state.currentExercise?.let { ex ->
                item {
                    ExerciseInfoCard(
                        exercise  = ex,
                        setNum    = state.currentSet,
                        repNum    = state.currentRep
                    )
                }
            }

            // ── Exercise queue ────────────────────────────────────────────────
            item {
                SectionHeader("Session Exercises",
                    subtitle = "${state.exercises.size} exercises · ${state.exercises.sumOf { it.sets * it.repsPerSet }} total reps")
            }
            items(state.exercises.size) { idx ->
                val ex        = state.exercises[idx]
                val isCurrent = idx == state.currentExerciseIdx
                val isDone    = idx < state.currentExerciseIdx

                ExerciseQueueRow(ex, isCurrent, isDone)
            }

            // ── Educational content ───────────────────────────────────────────
            item { Spacer(Modifier.height(8.dp)) }
            item {
                ExpandableCard("How It Works", Icons.Outlined.Info, Peak15Colors.Info) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        InfoSection("Kegel (Contraction)",
                            "Squeezes and lifts the pelvic floor muscles. Strengthens the muscles that support erection quality and ejaculatory control. The urethral sphincter and bulbocavernosus muscle are the primary targets.")
                        InfoSection("Reverse Kegel (Release)",
                            "Consciously opens and relaxes the pelvic floor. Equally important as contraction. A hypertonic (too tight) pelvic floor is more common than weakness and causes more dysfunction. Full release allows natural blood engorgement.")
                        InfoSection("Quick Flicks",
                            "Trains fast-twitch pelvic floor fibres. Important for ejaculatory control — the rapid voluntary inhibitory contraction that delays ejaculation.")
                        InfoSection("Warning Signs",
                            "Stop and seek medical evaluation if you experience: pain during exercises, pain in the perineum or pelvic region, or if symptoms worsen rather than improve.",
                            isWarning = true)
                    }
                }
            }

            // ── Recent sessions ───────────────────────────────────────────────
            if (state.recentSessions.isNotEmpty()) {
                item {
                    SectionHeader("Recent Sessions", subtitle = "Last ${state.recentSessions.size} logged")
                }
                items(state.recentSessions.take(5)) { session ->
                    RecentSessionRow(session)
                }
            }

            item { Spacer(Modifier.height(Peak15Spacing.xl)) }
        }
    }
}

// ─── Timer Card ───────────────────────────────────────────────────────────────

@Composable
private fun TimerCard(
    state  : PelvicFloorUiState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit
) {
    val timerColor = when (state.timerPhase) {
        TimerPhase.CONTRACT -> Peak15Colors.Primary
        TimerPhase.RELEASE  -> Peak15Colors.Secondary
        TimerPhase.REST     -> Peak15Colors.Warning
        TimerPhase.COMPLETE -> Peak15Colors.Success
        TimerPhase.IDLE     -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    // Pulse animation for contract phase
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue   = 1f,
        targetValue    = if (state.timerPhase == TimerPhase.CONTRACT && state.isRunning) 1.05f else 1f,
        animationSpec  = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Surface(
        shape          = MaterialTheme.shapes.extraLarge,
        color          = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier       = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Peak15Spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Peak15Spacing.md)
        ) {
            // Main timer ring
            CircularProgressIndicatorCustom(
                progress    = state.progressFraction,
                size        = 180.dp,
                strokeWidth = 12.dp,
                color       = timerColor,
                modifier    = Modifier.scale(pulseScale)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AnimatedContent(
                        targetState = state.secondsRemaining,
                        transitionSpec = {
                            fadeIn(tween(150)) togetherWith fadeOut(tween(150))
                        },
                        label = "timer_count"
                    ) { seconds ->
                        Text(
                            text  = formatTime(seconds),
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize   = 52.sp
                            ),
                            color = timerColor
                        )
                    }
                    AnimatedContent(
                        targetState = state.phaseLabel,
                        transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
                        label = "phase_label"
                    ) { label ->
                        Text(
                            label,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 2.sp
                            ),
                            color = timerColor
                        )
                    }
                }
            }

            // Phase instruction
            AnimatedContent(
                targetState = state.phaseInstruction,
                transitionSpec = { fadeIn(tween(300)) + slideInVertically { it / 4 } togetherWith
                    fadeOut(tween(200)) + slideOutVertically { -it / 4 } },
                label = "instruction"
            ) { instruction ->
                Text(
                    instruction,
                    style     = MaterialTheme.typography.bodyMedium,
                    color     = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier  = Modifier.padding(horizontal = 8.dp)
                )
            }

            // Set / rep counter
            if (state.timerPhase != TimerPhase.IDLE && state.timerPhase != TimerPhase.COMPLETE) {
                val ex = state.currentExercise
                if (ex != null) {
                    Text(
                        "Set ${state.currentSet}/${ex.sets}  ·  Rep ${state.currentRep}/${ex.repsPerSet}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (state.sessionCompleted) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = Peak15Colors.Success.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.CheckCircle, null,
                            tint = Peak15Colors.Success, modifier = Modifier.size(20.dp))
                        Text("Session complete! Great work.", style = MaterialTheme.typography.labelMedium,
                            color = Peak15Colors.Success)
                    }
                }
            }

            // Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.md),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // Reset
                OutlinedIconButton(onClick = onReset,
                    modifier = Modifier.size(48.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))) {
                    Icon(Icons.Filled.Refresh, "Reset", modifier = Modifier.size(20.dp))
                }

                // Start / Pause
                Button(
                    onClick = if (state.isRunning) onPause else onStart,
                    modifier = Modifier.height(56.dp).width(160.dp),
                    shape    = MaterialTheme.shapes.large,
                    colors   = ButtonDefaults.buttonColors(containerColor = timerColor)
                ) {
                    Icon(
                        if (state.isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        null, modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (state.isRunning) "Pause" else
                            if (state.timerPhase == TimerPhase.IDLE) "Start" else "Resume",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

// ─── Exercise Info Card ───────────────────────────────────────────────────────

@Composable
private fun ExerciseInfoCard(
    exercise: PelvicExercise,
    setNum  : Int,
    repNum  : Int
) {
    val typeColor = when (exercise.type) {
        PelvicExerciseType.KEGEL         -> Peak15Colors.Primary
        PelvicExerciseType.REVERSE_KEGEL -> Peak15Colors.Secondary
        PelvicExerciseType.QUICK_FLICK   -> Peak15Colors.Warning
        PelvicExerciseType.ELEVATOR      -> Peak15Colors.Peak
        PelvicExerciseType.BREATHING     -> Peak15Colors.Info
    }

    Surface(
        shape  = MaterialTheme.shapes.large,
        color  = typeColor.copy(alpha = 0.07f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Peak15Spacing.md),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(exercise.name, style = MaterialTheme.typography.titleMedium)
                    Text(exercise.type.name.replace('_',' '),
                        style = MaterialTheme.typography.labelSmall, color = typeColor)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Chip("${exercise.contractSeconds}s on", typeColor)
                    Chip("${exercise.releaseSeconds}s off", typeColor.copy(alpha = 0.6f))
                }
            }
            if (exercise.instructions.isNotEmpty()) {
                Text(exercise.instructions, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            }
        }
    }
}

@Composable
private fun Chip(text: String, color: Color) {
    Surface(shape = MaterialTheme.shapes.small, color = color.copy(alpha = 0.12f)) {
        Text(text, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.SemiBold)
    }
}

// ─── Exercise Queue Row ───────────────────────────────────────────────────────

@Composable
private fun ExerciseQueueRow(
    exercise : PelvicExercise,
    isCurrent: Boolean,
    isDone   : Boolean
) {
    val bgColor = when {
        isCurrent -> Peak15Colors.Secondary.copy(alpha = 0.10f)
        isDone    -> Peak15Colors.Success.copy(alpha = 0.07f)
        else      -> MaterialTheme.colorScheme.surface
    }
    Surface(
        shape    = MaterialTheme.shapes.medium,
        color    = bgColor,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp, 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(when {
                        isDone    -> Peak15Colors.Success.copy(alpha = 0.15f)
                        isCurrent -> Peak15Colors.Secondary.copy(alpha = 0.15f)
                        else      -> MaterialTheme.colorScheme.surfaceVariant
                    }),
                contentAlignment = Alignment.Center
            ) {
                if (isDone) {
                    Icon(Icons.Filled.Check, null,
                        tint = Peak15Colors.Success, modifier = Modifier.size(16.dp))
                } else {
                    Icon(
                        when (exercise.type) {
                            PelvicExerciseType.KEGEL         -> Icons.Outlined.FitnessCenter
                            PelvicExerciseType.REVERSE_KEGEL -> Icons.Outlined.OpenWith
                            PelvicExerciseType.QUICK_FLICK   -> Icons.Outlined.FlashOn
                            PelvicExerciseType.ELEVATOR      -> Icons.Outlined.LinearScale
                            PelvicExerciseType.BREATHING     -> Icons.Outlined.Air
                        },
                        null,
                        tint = if (isCurrent) Peak15Colors.Secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(exercise.name, style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Normal)
                Text("${exercise.sets} sets × ${exercise.repsPerSet} reps",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (isCurrent) {
                Icon(Icons.Filled.PlayArrow, null,
                    tint = Peak15Colors.Secondary, modifier = Modifier.size(16.dp))
            }
        }
    }
}

// ─── Info Section ─────────────────────────────────────────────────────────────

@Composable
private fun InfoSection(title: String, body: String, isWarning: Boolean = false) {
    val color = if (isWarning) Peak15Colors.Error else Peak15Colors.Info
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = color)
        Text(body, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
    }
}

// ─── Recent Session Row ───────────────────────────────────────────────────────

@Composable
private fun RecentSessionRow(session: com.peak15.data.local.entities.PelvicSessionEntity) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.FitnessCenter, null,
                tint = Peak15Colors.Secondary.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
            Text("Day ${session.day} · ${session.exerciseType.replace('_',' ')}",
                style = MaterialTheme.typography.bodySmall)
        }
        Text("${session.sets}×${session.repsCompleted} · ${session.durationSeconds / 60}min",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return if (m > 0) "${m}:${s.toString().padStart(2, '0')}" else "${s}s"
}


