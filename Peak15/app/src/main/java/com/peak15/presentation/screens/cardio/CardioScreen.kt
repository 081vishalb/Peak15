package com.peak15.presentation.screens.cardio

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peak15.data.local.entities.CardioSessionEntity
import com.peak15.presentation.components.*
import com.peak15.presentation.theme.*
import com.peak15.presentation.viewmodel.CardioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardioScreen(
    onBack: () -> Unit,
    vm    : CardioViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cardio Tracker", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Back") }
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

            // ── Activity type selector ─────────────────────────────────────
            item {
                SectionHeader("Select Activity")
                Spacer(Modifier.height(Peak15Spacing.sm))
                ActivityTypeSelector(
                    selected = state.selectedType,
                    onSelect = { vm.selectType(it) }
                )
            }

            // ── Live timer ────────────────────────────────────────────────
            item {
                CardioTimerCard(
                    elapsedSeconds = state.elapsedSeconds,
                    isRunning      = state.timerRunning,
                    activityType   = state.selectedType,
                    onStart        = { vm.startTimer() },
                    onPause        = { vm.pauseTimer() },
                    onStop         = { vm.stopAndLog() }
                )
            }

            // ── Heart rate zones guide ────────────────────────────────────
            item {
                ExpandableCard(
                    "Heart Rate Zones Guide",
                    Icons.Outlined.Favorite,
                    Peak15Colors.Error
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        HeartRateZoneRow("Zone 2 – Aerobic Base", "60–70% max HR",
                            "Conversational pace. Primary fat-burning zone. Builds mitochondrial density and endothelial function. Target for most days.",
                            Peak15Colors.Build)
                        HeartRateZoneRow("Zone 3 – Tempo", "71–80% max HR",
                            "Comfortably hard. Can speak short sentences. Used for performance days.",
                            Peak15Colors.Warning)
                        HeartRateZoneRow("Zone 4 – Threshold", "81–90% max HR",
                            "Hard effort. 20–30 min max sustainable. HIIT target zone.",
                            Peak15Colors.Accent)
                        HeartRateZoneRow("Zone 5 – Max", "91–100% max HR",
                            "All-out sprint effort. Only for 20–30 second intervals. Acute testosterone boost.",
                            Peak15Colors.Error)
                        InfoCard("Estimate max HR: 220 – your age. For an 28-year-old: max HR = 192. Zone 2 = 115–134 bpm.")
                    }
                }
            }

            // ── Why cardio matters ────────────────────────────────────────
            item {
                ExpandableCard("Why This Matters", Icons.Outlined.Info, Peak15Colors.Info) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        BulletPoint("Zone 2 cardio improves mitochondrial density and nitric oxide production — the same biological mechanism as PDE5 inhibitors (Viagra), achieved naturally.")
                        BulletPoint("Cardiovascular fitness is the #1 predictor of erectile function quality in men under 40.")
                        BulletPoint("HIIT acutely elevates testosterone for 30–90 minutes post-session. Chronic HIIT training raises baseline testosterone.")
                        BulletPoint("VO2max improvements begin at Day 7–10 with consistent Zone 2 training.")
                    }
                }
            }

            // ── Today's sessions ──────────────────────────────────────────
            if (state.sessions.isNotEmpty()) {
                item {
                    SectionHeader(
                        "Today's Sessions",
                        subtitle = "${state.sessions.size} logged"
                    )
                }
                items(state.sessions) { session ->
                    CardioSessionRow(session)
                }
            }

            item { Spacer(Modifier.height(Peak15Spacing.xl)) }
        }
    }
}

// ─── Activity Type Selector ───────────────────────────────────────────────────

private data class ActivityType(
    val key  : String,
    val label: String,
    val icon : ImageVector,
    val color: Color
)

private val activityTypes = listOf(
    ActivityType("WALK",        "Walk",     Icons.Outlined.DirectionsWalk,  Peak15Colors.Build),
    ActivityType("RUN",         "Run",      Icons.Outlined.DirectionsRun,   Peak15Colors.Primary),
    ActivityType("CYCLE",       "Cycle",    Icons.Outlined.DirectionsBike,  Peak15Colors.Accent),
    ActivityType("HIIT_SPRINT", "HIIT",     Icons.Outlined.ElectricBolt,            Peak15Colors.Error),
    ActivityType("SWIMMING",    "Swim",     Icons.Outlined.Water,            Peak15Colors.Info)
)

@Composable
private fun ActivityTypeSelector(selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        activityTypes.forEach { type ->
            val isSelected = selected == type.key
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSelect(type.key) },
                shape = MaterialTheme.shapes.medium,
                color = if (isSelected) type.color.copy(alpha = 0.15f)
                        else MaterialTheme.colorScheme.surface,
                border = if (isSelected)
                    BorderStroke(1.5.dp, type.color)
                else
                    BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        type.icon, null,
                        tint = if (isSelected) type.color
                               else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        type.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) type.color
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

// ─── Cardio Timer Card ────────────────────────────────────────────────────────

@Composable
private fun CardioTimerCard(
    elapsedSeconds: Int,
    isRunning     : Boolean,
    activityType  : String,
    onStart       : () -> Unit,
    onPause       : () -> Unit,
    onStop        : () -> Unit
) {
    val actType = activityTypes.find { it.key == activityType } ?: activityTypes[0]
    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60

    // Pulse animation when running
    val infiniteTransition = rememberInfiniteTransition(label = "cardio_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue  = 0.6f,
        targetValue   = if (isRunning) 1f else 0.6f,
        animationSpec = infiniteRepeatable(
            animation  = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
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
            // Activity icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(actType.color.copy(alpha = if (isRunning) pulseAlpha * 0.15f else 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(actType.icon, null, tint = actType.color, modifier = Modifier.size(32.dp))
            }

            // Timer display
            Text(
                text  = "%02d:%02d".format(minutes, seconds),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize   = 64.sp
                ),
                color = actType.color
            )

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatChip(label = "Minutes",  value = "$minutes",     color = actType.color)
                StatChip(label = "Seconds",  value = "${seconds}s",  color = actType.color)
                StatChip(
                    label = "Status",
                    value = if (isRunning) "Active" else if (elapsedSeconds > 0) "Paused" else "Ready",
                    color = if (isRunning) Peak15Colors.Success else actType.color
                )
            }

            // Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.sm),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                if (elapsedSeconds > 0) {
                    // Stop & log button
                    OutlinedButton(
                        onClick = onStop,
                        modifier = Modifier.height(52.dp),
                        shape  = MaterialTheme.shapes.large,
                        border = BorderStroke(1.dp, Peak15Colors.Error.copy(alpha = 0.6f)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Peak15Colors.Error
                        )
                    ) {
                        Icon(Icons.Filled.Stop, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Stop & Log")
                    }
                }

                // Start / Pause
                Button(
                    onClick  = if (isRunning) onPause else onStart,
                    modifier = Modifier.height(52.dp).weight(1f),
                    shape    = MaterialTheme.shapes.large,
                    colors   = ButtonDefaults.buttonColors(containerColor = actType.color)
                ) {
                    Icon(
                        if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        null, modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (isRunning) "Pause"
                        else if (elapsedSeconds > 0) "Resume"
                        else "Start ${actType.label}",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            if (elapsedSeconds >= 60) {
                Text(
                    "Logging at ${minutes}min. ${
                        when {
                            minutes >= 45 -> "Outstanding session! VO2max adaptation zone."
                            minutes >= 30 -> "Zone 2 aerobic adaptations happening now."
                            minutes >= 20 -> "Good session. Aim for 30–45 min."
                            else -> "Keep going — Zone 2 benefits kick in after 20 min."
                        }
                    }",
                    style     = MaterialTheme.typography.bodySmall,
                    color     = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun StatChip(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun HeartRateZoneRow(zone: String, range: String, desc: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .width(4.dp).height(48.dp)
                .clip(MaterialTheme.shapes.small)
                .background(color)
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(zone, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold), color = color)
                Text(range, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f))
        }
    }
}

@Composable
private fun BulletPoint(text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
        Box(modifier = Modifier.size(6.dp).offset(y = 5.dp).clip(CircleShape).background(Peak15Colors.Info))
        Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
    }
}

@Composable
private fun CardioSessionRow(session: CardioSessionEntity) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp, 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                val type = activityTypes.find { it.key == session.type } ?: activityTypes[0]
                Icon(type.icon, null, tint = type.color, modifier = Modifier.size(20.dp))
                Column {
                    Text(type.label, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                    Text("Day ${session.day}", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${session.durationMinutes}min", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Peak15Colors.Build)
                if (session.avgHeartRate > 0) {
                    Text("${session.avgHeartRate} bpm", style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
