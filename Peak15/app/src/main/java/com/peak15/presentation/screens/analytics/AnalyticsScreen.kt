package com.peak15.presentation.screens.analytics

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.peak15.data.local.entities.DailyMetricsEntity
import com.peak15.data.local.entities.DayProgressEntity
import com.peak15.presentation.components.*
import com.peak15.presentation.theme.*
import com.peak15.presentation.viewmodel.AnalyticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onBack: () -> Unit,
    vm    : AnalyticsViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Progress Analytics") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->

        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Peak15Colors.Primary)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(Peak15Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Peak15Spacing.md)
        ) {

            // ── Summary stats ─────────────────────────────────────────────
            item {
                SectionHeader("Summary")
                Spacer(Modifier.height(Peak15Spacing.sm))
                SummaryStatsGrid(state)
            }

            // ── Completion chart ──────────────────────────────────────────
            item {
                SectionHeader("Daily Completion", subtitle = "Percentage of tasks completed per day")
                Spacer(Modifier.height(Peak15Spacing.sm))
                CompletionBarChart(state.allProgress)
            }

            // ── Metrics trend ─────────────────────────────────────────────
            item {
                SectionHeader("Metrics Trend", subtitle = "Mood, Energy, Erection Quality (1–10)")
                Spacer(Modifier.height(Peak15Spacing.sm))
                MetricsTrendChart(state.allMetrics)
            }

            // ── Water trend ───────────────────────────────────────────────
            item {
                SectionHeader("Hydration", subtitle = "Daily water intake (ml)")
                Spacer(Modifier.height(Peak15Spacing.sm))
                WaterBarChart(state.waterTotals.map { it.day to it.totalMl })
            }

            // ── Habit consistency ─────────────────────────────────────────
            item {
                SectionHeader("Habit Consistency")
                Spacer(Modifier.height(Peak15Spacing.sm))
                HabitConsistencyGrid(state.allProgress)
            }

            // ── Insight cards ─────────────────────────────────────────────
            item { SectionHeader("Insights") }
            item { InsightCards(state) }

            item { Spacer(Modifier.height(Peak15Spacing.xl)) }
        }
    }
}

// ─── Summary Stats Grid ───────────────────────────────────────────────────────

@Composable
private fun SummaryStatsGrid(state: com.peak15.presentation.viewmodel.AnalyticsUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.sm)
    ) {
        MetricCard(
            label      = "Streak",
            value      = "${state.streak}",
            unit       = "days",
            icon       = Icons.Outlined.Whatshot,
            accentColor= Peak15Colors.Warning,
            modifier   = Modifier.weight(1f)
        )
        MetricCard(
            label      = "Workouts",
            value      = "${state.totalWorkouts}",
            unit       = "done",
            icon       = Icons.Outlined.FitnessCenter,
            accentColor= Peak15Colors.Foundation,
            modifier   = Modifier.weight(1f)
        )
    }
    Spacer(Modifier.height(Peak15Spacing.sm))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.sm)
    ) {
        MetricCard(
            label      = "Avg Sleep",
            value      = "%.1f".format(state.avgSleep),
            unit       = "h",
            icon       = Icons.Outlined.Bedtime,
            accentColor= Peak15Colors.Build,
            modifier   = Modifier.weight(1f)
        )
        MetricCard(
            label      = "Avg Mood",
            value      = "%.1f".format(state.avgMood),
            unit       = "/ 10",
            icon       = Icons.Outlined.SentimentSatisfiedAlt,
            accentColor= Peak15Colors.Secondary,
            modifier   = Modifier.weight(1f)
        )
    }
}

// ─── Completion Bar Chart (custom, no external lib needed) ────────────────────

@Composable
private fun CompletionBarChart(progress: List<DayProgressEntity>) {
    Surface(
        shape          = MaterialTheme.shapes.large,
        color          = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier       = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(Peak15Spacing.md)) {
            if (progress.isEmpty()) {
                EmptyChartPlaceholder("Complete some days to see progress here")
                return@Column
            }
            Row(
                modifier = Modifier.fillMaxWidth().height(120.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                (1..15).forEach { day ->
                    val p = progress.find { it.day == day }
                    val pct = (p?.completionPercent ?: 0) / 100f
                    val color = phaseColor(day)

                    Column(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        if (pct > 0) {
                            Text(
                                "${(pct * 100).toInt()}",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.sp),
                                color = color
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .fillMaxHeight(pct.coerceAtLeast(0.03f))
                                .background(
                                    if (pct > 0) color else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    MaterialTheme.shapes.extraSmall
                                )
                        )
                        Text(
                            "$day",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 3.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.md)) {
                LegendItem(Peak15Colors.Foundation, "Foundation")
                LegendItem(Peak15Colors.Build, "Build")
                LegendItem(Peak15Colors.Peak, "Peak")
            }
        }
    }
}

// ─── Metrics Trend Chart ──────────────────────────────────────────────────────

@Composable
private fun MetricsTrendChart(metrics: List<DailyMetricsEntity>) {
    Surface(
        shape          = MaterialTheme.shapes.large,
        color          = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier       = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(Peak15Spacing.md), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (metrics.isEmpty()) {
                EmptyChartPlaceholder("Log daily check-ins to see trends")
                return@Column
            }
            // Simple text-based trend for each metric
            listOf(
                Triple("Mood",           Peak15Colors.Foundation) { m: DailyMetricsEntity -> m.moodScore },
                Triple("Energy",         Peak15Colors.Build)       { m: DailyMetricsEntity -> m.energyScore },
                Triple("Erection Quality", Peak15Colors.Peak)     { m: DailyMetricsEntity -> m.erectionQualityScore }
            ).forEach { (label, color, getValue) ->
                MetricTrendRow(label, color, metrics, getValue)
            }
        }
    }
}

@Composable
private fun MetricTrendRow(
    label   : String,
    color   : Color,
    metrics : List<DailyMetricsEntity>,
    getValue: (DailyMetricsEntity) -> Int
) {
    val values = metrics.map { getValue(it) }
    val avg    = if (values.isEmpty()) 0f else values.average().toFloat()
    val latest = values.lastOrNull() ?: 0
    val trend  = if (values.size >= 2) latest - getValue(metrics[metrics.size - 2]) else 0

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = color)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("avg %.1f".format(avg), style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (trend > 0) Icons.Filled.TrendingUp
                        else if (trend < 0) Icons.Filled.TrendingDown
                        else Icons.Filled.TrendingFlat,
                        null,
                        tint = when {
                            trend > 0 -> Peak15Colors.Success
                            trend < 0 -> Peak15Colors.Error
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        if (trend > 0) "+$trend" else "$trend",
                        style = MaterialTheme.typography.labelSmall,
                        color = when {
                            trend > 0 -> Peak15Colors.Success
                            trend < 0 -> Peak15Colors.Error
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
        // Mini dot chart
        Row(
            modifier = Modifier.fillMaxWidth().height(28.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            metrics.forEach { m ->
                val v = getValue(m) / 10f
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(v.coerceAtLeast(0.1f))
                        .background(color.copy(alpha = 0.5f + v * 0.5f), MaterialTheme.shapes.extraSmall)
                )
            }
            // Fill remaining days
            repeat(15 - metrics.size) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(0.1f)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), MaterialTheme.shapes.extraSmall)
                )
            }
        }
    }
}

// ─── Water Bar Chart ──────────────────────────────────────────────────────────

@Composable
private fun WaterBarChart(data: List<Pair<Int, Int>>) {
    Surface(
        shape          = MaterialTheme.shapes.large,
        color          = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier       = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(Peak15Spacing.md)) {
            if (data.isEmpty()) {
                EmptyChartPlaceholder("Log water intake to see hydration trends")
                return@Column
            }
            val maxMl = data.maxOf { it.second }.coerceAtLeast(3500)
            Row(
                modifier = Modifier.fillMaxWidth().height(80.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                (1..15).forEach { day ->
                    val ml = data.find { it.first == day }?.second ?: 0
                    val fraction = (ml.toFloat() / maxMl).coerceIn(0f, 1f)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(fraction.coerceAtLeast(0.03f))
                            .background(
                                if (ml >= 3000) Peak15Colors.Info else Peak15Colors.Info.copy(alpha = 0.4f),
                                MaterialTheme.shapes.extraSmall
                            )
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Day 1", style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Target: 3.5L/day", style = MaterialTheme.typography.labelSmall, color = Peak15Colors.Info)
                Text("Day 15", style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

// ─── Habit Consistency Grid ───────────────────────────────────────────────────

@Composable
private fun HabitConsistencyGrid(progress: List<DayProgressEntity>) {
    Surface(
        shape          = MaterialTheme.shapes.large,
        color          = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier       = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(Peak15Spacing.md), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            data class HabitStat(val label: String, val color: Color, val count: Int, val total: Int)
            val habits = listOf(
                HabitStat("Workout",      Peak15Colors.Foundation, progress.count { it.workoutCompleted },     progress.size),
                HabitStat("Pelvic Floor", Peak15Colors.Secondary,  progress.count { it.pelvicFloorCompleted}, progress.size),
                HabitStat("Cardio",       Peak15Colors.Info,       progress.count { it.cardioCompleted },     progress.size),
                HabitStat("Supplements",  Peak15Colors.Warning,    progress.count { it.supplementsTaken },    progress.size),
                HabitStat("Confidence",   Peak15Colors.Peak,       progress.count { it.confidenceChallengeCompleted }, progress.size)
            )

            habits.forEach { habit ->
                val fraction = if (habit.total > 0) habit.count.toFloat() / 15f else 0f
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(habit.label, style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.width(100.dp), color = habit.color)
                    AnimatedLinearProgress(fraction, habit.color, modifier = Modifier.weight(1f))
                    Text(
                        "${habit.count}/15",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(36.dp)
                    )
                }
            }
        }
    }
}

// ─── Insight Cards ────────────────────────────────────────────────────────────

@Composable
private fun InsightCards(state: com.peak15.presentation.viewmodel.AnalyticsUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (state.streak >= 7) {
            InsightCard(
                "🔥 ${state.streak}-day streak!",
                "You're in the Build phase rhythm. Consistency is the #1 predictor of long-term results.",
                Peak15Colors.Warning
            )
        }
        if (state.avgSleep < 7f && state.allSleep.isNotEmpty()) {
            InsightCard(
                "😴 Sleep below target",
                "Average ${state.avgSleep.let { "%.1f".format(it) }}h vs 8h target. Testosterone is synthesised during REM sleep — this is your highest-leverage improvement area.",
                Peak15Colors.Error
            )
        }
        if (state.avgEQ > 7f && state.allMetrics.isNotEmpty()) {
            InsightCard(
                "⚡ Erection quality trending well",
                "Average ${state.avgEQ.let { "%.1f".format(it) }}/10. Pelvic floor training and NO pathway improvements are working.",
                Peak15Colors.Success
            )
        }
        if (state.totalWorkouts >= 5) {
            InsightCard(
                "💪 ${state.totalWorkouts} workouts completed",
                "Testosterone is elevated from consistent resistance training. Compound lifts + HIIT are the most evidence-supported interventions.",
                Peak15Colors.Foundation
            )
        }
        if (state.allProgress.isEmpty()) {
            InsightCard(
                "🚀 Start your journey",
                "Log your first day to begin tracking your progress. The 15-day program is evidence-based and cumulative.",
                Peak15Colors.Primary
            )
        }
    }
}

@Composable
private fun InsightCard(title: String, body: String, color: Color) {
    Surface(
        shape    = MaterialTheme.shapes.large,
        color    = color.copy(alpha = 0.08f),
        border   = BorderStroke(0.5.dp, color.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(Peak15Spacing.md), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold), color = color)
            Text(body, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).background(color, MaterialTheme.shapes.extraSmall))
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun EmptyChartPlaceholder(message: String) {
    Box(modifier = Modifier.fillMaxWidth().height(80.dp), contentAlignment = Alignment.Center) {
        Text(message, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
