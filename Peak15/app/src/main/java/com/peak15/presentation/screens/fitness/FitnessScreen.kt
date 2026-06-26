package com.peak15.presentation.screens.fitness

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peak15.domain.model.*
import com.peak15.presentation.components.*
import com.peak15.presentation.theme.*
import com.peak15.presentation.viewmodel.DayDetailViewModel
import com.peak15.presentation.viewmodel.RoadmapViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessScreen(
    onWorkoutClick: (Int) -> Unit,
    onBack        : () -> Unit,
    vm            : RoadmapViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout Plans") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(Peak15Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Peak15Spacing.sm)
        ) {
            item { SectionHeader("All 15 Days", subtitle = "Tap a day to see exercises") }
            items(state.days) { day ->
                WorkoutDayCard(day, onClick = { onWorkoutClick(day.day) })
            }
            item { Spacer(Modifier.height(Peak15Spacing.xl)) }
        }
    }
}

@Composable
private fun WorkoutDayCard(day: DayProgram, onClick: () -> Unit) {
    Surface(
        modifier       = Modifier.fillMaxWidth().clickable { onClick() },
        shape          = MaterialTheme.shapes.large,
        color          = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(Peak15Spacing.md),
            horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val col = phaseColor(day.day)
            Box(
                modifier = Modifier.size(44.dp)
                    .background(col.copy(alpha = 0.12f), MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Text("${day.day}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = col)
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(day.workout.name, style = MaterialTheme.typography.titleSmall)
                Text("${day.workout.exercises.size} exercises · ${day.workout.totalDurationMinutes}min · ${day.workout.type.name.replace('_',' ')}",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Filled.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailScreen(
    day: Int,
    onBack: () -> Unit,
    vm : DayDetailViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(day) { vm.loadDay(day) }
    val program = state.program ?: return
    val phaseCol = phaseColor(day)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Column {
                    Text(program.workout.name)
                    Text("Day $day · ${program.workout.type.name.replace('_', ' ')} · ${program.workout.totalDurationMinutes}min",
                        style = MaterialTheme.typography.labelSmall, color = phaseCol)
                }},
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(Peak15Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Peak15Spacing.sm)
        ) {
            if (program.workout.notes.isNotEmpty()) {
                item { InfoCard(program.workout.notes) }
            }
            item { SectionHeader("Exercises", subtitle = "${program.workout.exercises.size} movements") }
            items(program.workout.exercises.indices.toList()) { idx ->
                ExerciseCard(program.workout.exercises[idx], idx + 1, phaseCol)
            }
            item { Spacer(Modifier.height(Peak15Spacing.xl)) }
        }
    }
}

@Composable
private fun ExerciseCard(ex: Exercise, number: Int, accentColor: androidx.compose.ui.graphics.Color) {
    var expanded by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
        shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(Peak15Spacing.md), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(36.dp).background(accentColor.copy(alpha = 0.12f), MaterialTheme.shapes.medium),
                    contentAlignment = Alignment.Center) {
                    Text("$number", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = accentColor)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(ex.name, style = MaterialTheme.typography.titleSmall)
                    Text(buildString {
                        if (ex.sets > 1) append("${ex.sets} sets × ")
                        append(ex.reps)
                        if (ex.restSeconds > 0) append(" · ${ex.restSeconds}s rest")
                    }, style = MaterialTheme.typography.bodySmall, color = accentColor)
                }
                Icon(if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore, null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (expanded) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Text(ex.instructions, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                if (ex.musclesWorked.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        ex.musclesWorked.forEach { muscle ->
                            Surface(shape = MaterialTheme.shapes.extraSmall, color = accentColor.copy(alpha = 0.10f)) {
                                Text(muscle, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall, color = accentColor)
                            }
                        }
                    }
                }
            }
        }
    }
}
