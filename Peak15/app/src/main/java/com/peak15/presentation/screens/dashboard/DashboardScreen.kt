package com.peak15.presentation.screens.dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peak15.presentation.components.*
import com.peak15.presentation.navigation.Routes
import com.peak15.presentation.theme.*
import com.peak15.presentation.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigate: (String) -> Unit,
    vm        : DashboardViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    var showSleepDialog    by remember { mutableStateOf(false) }
    var showMetricsDialog  by remember { mutableStateOf(false) }

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Peak15Colors.Primary)
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Peak15",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            "Day ${state.currentDay} of 15",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    if (state.streak > 0) {
                        StreakBadge(state.streak, Modifier.padding(end = 8.dp))
                    }
                    IconButton(onClick = { onNavigate(Routes.SETTINGS) }) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = Peak15Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Peak15Spacing.lg)
        ) {
            Spacer(Modifier.height(4.dp))

            // ── Hero completion ring ──────────────────────────────────────────
            HeroCompletionCard(
                day        = state.currentDay,
                phase      = state.dayProgram?.phase?.label ?: "",
                dayTitle   = state.dayProgram?.title ?: "",
                completion = (state.progress?.completionPercent ?: 0) / 100f,
                streak     = state.streak
            )

            // ── Quick metrics ─────────────────────────────────────────────────
            SectionHeader(title = "Today's Metrics")

            val waterFraction = (state.waterTodayMl.toFloat() / state.waterTargetMl).coerceIn(0f, 1f)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.sm)
            ) {
                MetricCard(
                    label      = "Water",
                    value      = "%.1f".format(state.waterTodayMl / 1000f),
                    unit       = "L",
                    icon       = Icons.Outlined.WaterDrop,
                    accentColor= Peak15Colors.Info,
                    modifier   = Modifier.weight(1f),
                    onClick    = {}
                )
                MetricCard(
                    label      = "Sleep",
                    value      = if ((state.sleepLog?.durationHours ?: 0f) > 0)
                                     "%.1f".format(state.sleepLog!!.durationHours)
                                 else "--",
                    unit       = if ((state.sleepLog?.durationHours ?: 0f) > 0) "h" else "",
                    icon       = Icons.Outlined.Bedtime,
                    accentColor= Peak15Colors.Build,
                    modifier   = Modifier.weight(1f),
                    onClick    = { showSleepDialog = true }
                )
            }

            // ── Water intake ─────────────────────────────────────────────────
            WaterIntakeSection(
                currentMl = state.waterTodayMl,
                targetMl  = state.waterTargetMl,
                onAdd     = { vm.addWater(it) }
            )

            // ── Daily tasks ──────────────────────────────────────────────────
            SectionHeader(
                title    = "Today's Tasks",
                subtitle = state.dayProgram?.title ?: ""
            )

            DailyTasksList(
                workoutCompleted    = state.progress?.workoutCompleted ?: false,
                pelvicCompleted     = state.progress?.pelvicFloorCompleted ?: false,
                cardioCompleted     = state.progress?.cardioCompleted ?: false,
                supplementsComplete = state.progress?.supplementsTaken ?: false,
                onWorkout           = {
                    vm.toggleWorkout()
                    onNavigate(Routes.workoutDetail(state.currentDay))
                },
                onPelvic            = {
                    vm.togglePelvicFloor()
                    onNavigate(Routes.PELVIC_FLOOR)
                },
                onCardio            = {
                    vm.toggleCardio()
                    onNavigate(Routes.CARDIO)
                },
                onSupplements       = { vm.toggleSupplements() },
                onNutrition         = { onNavigate(Routes.NUTRITION) },
                onConfidence        = { onNavigate(Routes.CONFIDENCE) },
                onRecovery          = { onNavigate(Routes.RECOVERY) }
            )

            // ── Daily metrics check-in ────────────────────────────────────────
            MetricsCheckInCard(
                metrics = state.metrics,
                onClick = { showMetricsDialog = true }
            )

            // ── Today's supplement list ────────────────────────────────────────
            state.dayProgram?.supplements?.takeIf { it.isNotEmpty() }?.let { sups ->
                SectionHeader(title = "Supplements")
                SupplementList(supplements = sups)
            }

            // ── Avoid list ────────────────────────────────────────────────────
            state.dayProgram?.thingsToAvoid?.takeIf { it.isNotEmpty() }?.let { avoid ->
                SectionHeader(title = "Avoid Today")
                AvoidList(items = avoid)
            }

            Spacer(Modifier.height(Peak15Spacing.xl))
        }
    }

    // ── Sleep dialog ──────────────────────────────────────────────────────────
    if (showSleepDialog) {
        SleepLogDialog(
            initialHours = state.sleepLog?.durationHours ?: 8f,
            onDismiss    = { showSleepDialog = false },
            onConfirm    = { hours, quality ->
                vm.logSleep(hours, quality)
                showSleepDialog = false
            }
        )
    }

    // ── Metrics dialog ────────────────────────────────────────────────────────
    if (showMetricsDialog) {
        DailyMetricsDialog(
            initial  = state.metrics,
            onDismiss= { showMetricsDialog = false },
            onConfirm= { mood, energy, eq ->
                vm.saveMetrics(mood, energy, eq)
                showMetricsDialog = false
            }
        )
    }
}

// ─── Hero Card ───────────────────────────────────────────────────────────────

@Composable
private fun HeroCompletionCard(
    day       : Int,
    phase     : String,
    dayTitle  : String,
    completion: Float,
    streak    : Int
) {
    val color = phaseColor(day)

    Surface(
        shape          = MaterialTheme.shapes.extraLarge,
        color          = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier       = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(Peak15Spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.lg)
        ) {
            CircularProgressIndicatorCustom(
                progress   = completion,
                size       = 100.dp,
                strokeWidth= 9.dp,
                color      = color
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${(completion * 100).toInt()}%",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = color
                    )
                    Text(
                        "done",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                PhaseBadge(day)
                Text(
                    "Day $day · $dayTitle",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                AnimatedLinearProgress(completion, color)
                Text(
                    "${(completion * 100).toInt()}% of today's program complete",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ─── Water Intake Section ─────────────────────────────────────────────────────

@Composable
private fun WaterIntakeSection(
    currentMl: Int,
    targetMl : Int,
    onAdd    : (Int) -> Unit
) {
    val fraction = (currentMl.toFloat() / targetMl).coerceIn(0f, 1f)

    Surface(
        shape          = MaterialTheme.shapes.large,
        color          = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier       = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Peak15Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Peak15Spacing.sm)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.WaterDrop, null,
                        tint = Peak15Colors.Info, modifier = Modifier.size(20.dp))
                    Text("Hydration", style = MaterialTheme.typography.titleSmall)
                }
                Text(
                    "${currentMl}ml / ${targetMl}ml",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedLinearProgress(fraction, Peak15Colors.Info, height = 8.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.sm)
            ) {
                listOf(200 to "200ml", 250 to "250ml", 500 to "500ml", 750 to "750ml").forEach { (ml, label) ->
                    WaterAddButton(label, onClick = { onAdd(ml) }, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// ─── Daily Tasks List ─────────────────────────────────────────────────────────

@Composable
private fun DailyTasksList(
    workoutCompleted   : Boolean,
    pelvicCompleted    : Boolean,
    cardioCompleted    : Boolean,
    supplementsComplete: Boolean,
    onWorkout          : () -> Unit,
    onPelvic           : () -> Unit,
    onCardio           : () -> Unit,
    onSupplements      : () -> Unit,
    onNutrition        : () -> Unit,
    onConfidence       : () -> Unit,
    onRecovery         : () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Peak15Spacing.sm)) {
        TaskRow("Workout", workoutCompleted,     Icons.Outlined.FitnessCenter,
            Peak15Colors.Foundation, onWorkout)
        TaskRow("Pelvic Floor Training", pelvicCompleted, Icons.Outlined.SelfImprovement,
            Peak15Colors.Secondary, onPelvic)
        TaskRow("Cardio", cardioCompleted,       Icons.Outlined.DirectionsRun,
            Peak15Colors.Info, onCardio)
        TaskRow("Supplements", supplementsComplete, Icons.Outlined.Medication,
            Peak15Colors.Warning, onSupplements)
        TaskRow("Nutrition Plan", false,          Icons.Outlined.Restaurant,
            Peak15Colors.Build, onNutrition)
        TaskRow("Confidence Challenge", false,    Icons.Outlined.Psychology,
            Peak15Colors.Peak, onConfidence)
        TaskRow("Recovery Routine", false,        Icons.Outlined.Spa,
            Peak15Colors.Secondary, onRecovery)
    }
}

// ─── Metrics Check-In Card ────────────────────────────────────────────────────

@Composable
private fun MetricsCheckInCard(
    metrics: com.peak15.data.local.entities.DailyMetricsEntity?,
    onClick: () -> Unit
) {
    Surface(
        modifier  = Modifier.fillMaxWidth().clickable { onClick() },
        shape     = MaterialTheme.shapes.large,
        color     = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(Peak15Spacing.md),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Daily Check-In", style = MaterialTheme.typography.titleSmall)
                if (metrics != null) {
                    Text(
                        "Mood ${metrics.moodScore}/10 · Energy ${metrics.energyScore}/10 · EQ ${metrics.erectionQualityScore}/10",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        "Tap to log mood, energy & erection quality",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(Icons.Outlined.Edit, null, tint = Peak15Colors.Primary)
        }
    }
}

// ─── Supplement List ──────────────────────────────────────────────────────────

@Composable
private fun SupplementList(supplements: List<com.peak15.domain.model.Supplement>) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        supplements.forEach { sup ->
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp, 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Medication, null,
                        tint = Peak15Colors.Warning, modifier = Modifier.size(18.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("${sup.name} · ${sup.dosage}", style = MaterialTheme.typography.labelMedium)
                        Text(sup.timing, style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    val evidenceColor = when (sup.evidenceLevel) {
                        com.peak15.domain.model.EvidenceLevel.STRONG   -> Peak15Colors.Success
                        com.peak15.domain.model.EvidenceLevel.MODERATE -> Peak15Colors.Warning
                        com.peak15.domain.model.EvidenceLevel.EMERGING -> Peak15Colors.Info
                    }
                    Text(
                        sup.evidenceLevel.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall,
                        color = evidenceColor
                    )
                }
            }
        }
    }
}

// ─── Avoid List ───────────────────────────────────────────────────────────────

@Composable
private fun AvoidList(items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach { item ->
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = Peak15Colors.Error.copy(alpha = 0.07f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp, 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Outlined.Block, null,
                        tint = Peak15Colors.Error, modifier = Modifier.size(16.dp).padding(top = 1.dp))
                    Text(item, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f))
                }
            }
        }
    }
}

// ─── Sleep Dialog ─────────────────────────────────────────────────────────────

@Composable
private fun SleepLogDialog(
    initialHours: Float,
    onDismiss   : () -> Unit,
    onConfirm   : (Float, Int) -> Unit
) {
    var hours   by remember { mutableStateOf(initialHours) }
    var quality by remember { mutableStateOf(7) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Sleep") },
        text  = {
            Column(verticalArrangement = Arrangement.spacedBy(Peak15Spacing.md)) {
                Text("How many hours did you sleep?",
                    style = MaterialTheme.typography.bodyMedium)
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Hours: ${"%.1f".format(hours)}h",
                        style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                Slider(
                    value = hours, onValueChange = { hours = it },
                    valueRange = 4f..12f,
                    colors = SliderDefaults.colors(thumbColor = Peak15Colors.Build, activeTrackColor = Peak15Colors.Build)
                )
                ScoreSlider("Sleep Quality", quality, { quality = it }, Peak15Colors.Build)
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(hours, quality) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// ─── Daily Metrics Dialog ─────────────────────────────────────────────────────

@Composable
private fun DailyMetricsDialog(
    initial  : com.peak15.data.local.entities.DailyMetricsEntity?,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, Int) -> Unit
) {
    var mood    by remember { mutableStateOf(initial?.moodScore ?: 5) }
    var energy  by remember { mutableStateOf(initial?.energyScore ?: 5) }
    var eq      by remember { mutableStateOf(initial?.erectionQualityScore ?: 5) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Daily Check-In") },
        text  = {
            Column(verticalArrangement = Arrangement.spacedBy(Peak15Spacing.md)) {
                InfoCard("These scores track your progress over 15 days. Be honest — the trend matters more than individual days.")
                Spacer(Modifier.height(4.dp))
                ScoreSlider("Mood", mood, { mood = it }, Peak15Colors.Foundation)
                ScoreSlider("Energy", energy, { energy = it }, Peak15Colors.Build)
                ScoreSlider("Erection Quality (morning)", eq, { eq = it }, Peak15Colors.Peak)
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(mood, energy, eq) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
