package com.peak15.presentation.screens.roadmap

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peak15.data.local.entities.DayProgressEntity
import com.peak15.domain.model.*
import com.peak15.presentation.components.*
import com.peak15.presentation.theme.*
import com.peak15.presentation.viewmodel.DayDetailViewModel
import com.peak15.presentation.viewmodel.RoadmapViewModel

// ─── Roadmap Screen ───────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapScreen(
    onDayClick: (Int) -> Unit,
    vm        : RoadmapViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("15-Day Program", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
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
            verticalArrangement = Arrangement.spacedBy(Peak15Spacing.sm)
        ) {
            // Phase headers + day cards
            ProgramDataPhases.forEach { (phase, range) ->
                item {
                    Spacer(Modifier.height(Peak15Spacing.sm))
                    PhaseHeader(phase = phase, range = range)
                    Spacer(Modifier.height(Peak15Spacing.sm))
                }
                items(range.toList()) { dayNum ->
                    val dayProgram = state.days.getOrNull(dayNum - 1)
                    val progress   = state.progress[dayNum]
                    val isCurrent  = dayNum == state.currentDay
                    val isLocked   = dayNum > state.currentDay

                    if (dayProgram != null) {
                        DayCard(
                            day      = dayProgram,
                            progress = progress,
                            isCurrent= isCurrent,
                            isLocked = isLocked,
                            onClick  = { if (!isLocked) onDayClick(dayNum) }
                        )
                    }
                }
            }
            item { Spacer(Modifier.height(Peak15Spacing.xl)) }
        }
    }
}

private val ProgramDataPhases = listOf(
    Phase.FOUNDATION to 1..5,
    Phase.BUILD       to 6..10,
    Phase.PEAK        to 11..15
)

@Composable
private fun PhaseHeader(phase: Phase, range: IntRange) {
    val color = when (phase) {
        Phase.FOUNDATION -> Peak15Colors.Foundation
        Phase.BUILD       -> Peak15Colors.Build
        Phase.PEAK        -> Peak15Colors.Peak
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(32.dp)
                .clip(MaterialTheme.shapes.small)
                .background(color)
        )
        Column {
            Text(
                phase.label,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
            Text(
                "Days ${range.first}–${range.last} · ${phase.description.substringAfter(": ")}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DayCard(
    day      : DayProgram,
    progress : DayProgressEntity?,
    isCurrent: Boolean,
    isLocked : Boolean,
    onClick  : () -> Unit
) {
    val phaseColor = phaseColor(day.day)
    val completion = progress?.completionPercent ?: 0
    val isComplete = completion >= 80

    val borderColor = when {
        isCurrent -> phaseColor
        isLocked  -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        else      -> MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
    }
    val borderWidth = if (isCurrent) 1.5.dp else 0.5.dp

    Surface(
        modifier       = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isLocked) { onClick() }
            .border(borderWidth, borderColor, MaterialTheme.shapes.large),
        shape          = MaterialTheme.shapes.large,
        color          = if (isCurrent) phaseColor.copy(alpha = 0.05f)
                         else MaterialTheme.colorScheme.surface,
        tonalElevation = if (isCurrent) 0.dp else 1.dp
    ) {
        Row(
            modifier = Modifier.padding(Peak15Spacing.md),
            horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Day number circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(
                        if (isComplete) phaseColor.copy(alpha = 0.15f)
                        else if (isCurrent) phaseColor.copy(alpha = 0.12f)
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isComplete) {
                    Icon(Icons.Filled.CheckCircle, null,
                        tint = phaseColor, modifier = Modifier.size(24.dp))
                } else if (isLocked) {
                    Icon(Icons.Outlined.Lock, null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp))
                } else {
                    Text(
                        "${day.day}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isCurrent) phaseColor else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Content
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isCurrent) {
                        Surface(
                            shape = MaterialTheme.shapes.extraSmall,
                            color = phaseColor
                        ) {
                            Text(
                                "TODAY",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                    }
                    Text(
                        day.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (isLocked) MaterialTheme.colorScheme.onSurfaceVariant
                                else MaterialTheme.colorScheme.onSurface
                    )
                }

                if (!isLocked) {
                    Text(
                        "${day.workout.type.name.replace('_',' ')} · ${day.workout.totalDurationMinutes}min · ${day.pelvicFloor.totalMinutes}min pelvic",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (completion > 0) {
                        AnimatedLinearProgress(
                            progress = completion / 100f,
                            color    = phaseColor,
                            height   = 3.dp
                        )
                    }
                }
            }

            // Right chevron
            if (!isLocked) {
                Icon(
                    Icons.Filled.ChevronRight, null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ─── Day Detail Screen ────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayDetailScreen(
    day         : Int,
    onBack      : () -> Unit,
    onWorkout   : (Int) -> Unit,
    onPelvic    : () -> Unit,
    onCardio    : () -> Unit,
    onNutrition : () -> Unit,
    onConfidence: () -> Unit,
    onRecovery  : () -> Unit,
    vm          : DayDetailViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(day) { vm.loadDay(day) }

    val program   = state.program ?: return
    val phaseCol  = phaseColor(day)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Day $day · ${program.title}",
                            style = MaterialTheme.typography.titleLarge)
                        Text(program.phase.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = phaseCol)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
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
            verticalArrangement = Arrangement.spacedBy(Peak15Spacing.md)
        ) {
            Spacer(Modifier.height(4.dp))

            // Quick nav tiles
            Text("Jump to section", style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                val tiles = listOf(
                    Triple("Workout",    Icons.Outlined.FitnessCenter) { onWorkout(day) },
                    Triple("Pelvic",     Icons.Outlined.SelfImprovement) { onPelvic() },
                    Triple("Cardio",     Icons.Outlined.DirectionsRun) { onCardio() },
                    Triple("Nutrition",  Icons.Outlined.Restaurant) { onNutrition() },
                    Triple("Confidence", Icons.Outlined.Psychology) { onConfidence() },
                    Triple("Recovery",   Icons.Outlined.Spa) { onRecovery() }
                )
                items(tiles) { (label, icon, action) ->
                    QuickNavTile(label, icon, phaseCol, action)
                }
            }

            // Morning routine
            ExpandableCard("Morning Routine", Icons.Outlined.WbSunny, phaseCol,
                initiallyExpanded = true) {
                Text(program.morningRoutine, style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp)
            }

            // Pelvic floor session details
            ExpandableCard("Pelvic Floor Session", Icons.Outlined.SelfImprovement, Peak15Colors.Secondary) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Focus: ${program.pelvicFloor.focusType.name.replace('_',' ')}",
                        style = MaterialTheme.typography.labelMedium, color = Peak15Colors.Secondary)
                    program.pelvicFloor.exercises.forEach { ex ->
                        PelvicExerciseRow(ex)
                    }
                    if (program.pelvicFloor.notes.isNotEmpty()) {
                        InfoCard(program.pelvicFloor.notes, color = Peak15Colors.Secondary)
                    }
                }
            }

            // Nutrition
            ExpandableCard("Nutrition Plan", Icons.Outlined.Restaurant, Peak15Colors.Build) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    NutritionSummaryRow(program.nutrition)
                    program.nutrition.meals.forEach { meal ->
                        MealRow(meal)
                    }
                    if (program.nutrition.specialFoods.isNotEmpty()) {
                        Text("Key foods today:", style = MaterialTheme.typography.labelMedium,
                            color = Peak15Colors.Build)
                        program.nutrition.specialFoods.forEach { food ->
                            Text("• $food", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }

            // Mental performance
            ExpandableCard("Mental Performance", Icons.Outlined.Psychology, Peak15Colors.Peak) {
                Text(program.mentalPerformance, style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp)
            }

            // Why it works
            ExpandableCard("Why It Works", Icons.Outlined.Info, Peak15Colors.Info) {
                Text(program.whyItWorks, style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp)
                Spacer(Modifier.height(8.dp))
                InfoCard(
                    "Medical warning: Seek evaluation if you experience pelvic pain during Kegel exercises, sudden erectile dysfunction lasting 3+ days, genital numbness, or chest pain during exercise.",
                    icon  = Icons.Outlined.Warning,
                    color = Peak15Colors.Error
                )
            }

            // Things to avoid
            if (program.thingsToAvoid.isNotEmpty()) {
                ExpandableCard("Things to Avoid", Icons.Outlined.Block, Peak15Colors.Error) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        program.thingsToAvoid.forEach { item ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Filled.Remove, null, tint = Peak15Colors.Error,
                                    modifier = Modifier.size(16.dp).padding(top = 3.dp))
                                Text(item, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(Peak15Spacing.xl))
        }
    }
}

@Composable
private fun QuickNavTile(
    label  : String,
    icon   : ImageVector,
    color  : Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape    = MaterialTheme.shapes.medium,
        color    = color.copy(alpha = 0.10f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = color,
                fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun PelvicExerciseRow(ex: PelvicExercise) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = Peak15Colors.Secondary.copy(alpha = 0.07f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = MaterialTheme.shapes.extraSmall, color = Peak15Colors.Secondary.copy(alpha = 0.2f)) {
                    Text(ex.type.name.replace('_',' '),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall, color = Peak15Colors.Secondary,
                        fontWeight = FontWeight.SemiBold)
                }
                Text(ex.name, style = MaterialTheme.typography.titleSmall)
            }
            Text(
                "${ex.sets} sets × ${ex.repsPerSet} reps · ${ex.contractSeconds}s contract / ${ex.releaseSeconds}s release",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (ex.instructions.isNotEmpty()) {
                Text(ex.instructions, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            }
        }
    }
}

@Composable
private fun NutritionSummaryRow(nutrition: NutritionPlan) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf(
            "Calories" to "${nutrition.totalCalories}kcal",
            "Protein"  to "${nutrition.proteinGrams}g",
            "Carbs"    to "${nutrition.carbGrams}g",
            "Fat"      to "${nutrition.fatGrams}g"
        ).forEach { (label, value) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(value, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Peak15Colors.Build)
                Text(label, style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun MealRow(meal: Meal) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.width(80.dp)) {
            Text(meal.name, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
            Text(meal.time, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Column(modifier = Modifier.weight(1f)) {
            meal.foods.forEach { food ->
                Text("• $food", style = MaterialTheme.typography.bodySmall)
            }
        }
        Text("${meal.proteinGrams}g P", style = MaterialTheme.typography.labelSmall,
            color = Peak15Colors.Build)
    }
}


