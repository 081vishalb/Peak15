package com.peak15.presentation.screens.nutrition

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peak15.domain.model.Meal
import com.peak15.domain.model.NutritionPlan
import com.peak15.presentation.components.ExpandableCard
import com.peak15.presentation.components.InfoCard
import com.peak15.presentation.components.SectionHeader
import com.peak15.presentation.theme.Peak15Colors
import com.peak15.presentation.theme.Peak15Spacing
import com.peak15.presentation.viewmodel.DayDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen(
    onBack: () -> Unit,
    vm    : DayDetailViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { vm.loadDay(1) }

    val nutrition = state.program?.nutrition

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nutrition Plan") },
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
            modifier        = Modifier.fillMaxSize().padding(padding),
            contentPadding  = PaddingValues(Peak15Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Peak15Spacing.md)
        ) {
            if (nutrition != null) {

                item { MacroOverviewCard(nutrition) }

                item {
                    Surface(
                        shape    = MaterialTheme.shapes.large,
                        color    = Peak15Colors.Info.copy(alpha = 0.08f),
                        border   = BorderStroke(0.5.dp, Peak15Colors.Info.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(Peak15Spacing.md),
                            horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.md),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.WaterDrop, null,
                                tint = Peak15Colors.Info, modifier = Modifier.size(28.dp))
                            Column {
                                Text("Daily Water Target", style = MaterialTheme.typography.titleSmall)
                                Text("${nutrition.waterTargetLiters}L per day",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Peak15Colors.Info, fontWeight = FontWeight.SemiBold)
                                Text("Sip consistently throughout the day, not all at once.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }

                item { SectionHeader("Meals Today") }

                nutrition.meals.forEach { meal ->
                    item { MealDetailCard(meal) }
                }

                if (nutrition.specialFoods.isNotEmpty()) {
                    item {
                        ExpandableCard("Key Foods & Why", Icons.Outlined.Grade, Peak15Colors.Build) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                nutrition.specialFoods.forEach { food ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Icon(Icons.Filled.CheckCircle, null,
                                            tint = Peak15Colors.Build,
                                            modifier = Modifier.size(16.dp).padding(top = 1.dp))
                                        Text(food, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    ExpandableCard("Nutrition Science", Icons.Outlined.Biotech, Peak15Colors.Info) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            NutrientFactRow("Protein",
                                "1.6–2.2g/kg body weight optimal for muscle protein synthesis. For 73kg: target 120–160g minimum, 180g optimal.")
                            NutrientFactRow("Zinc",
                                "Direct cofactor for testosterone synthesis. Found in red meat, oysters, pumpkin seeds. Most men are deficient.")
                            NutrientFactRow("Omega-3 (EPA + DHA)",
                                "2g/day minimum. Improves endothelial function — directly improves erectile quality via nitric oxide pathways.")
                            NutrientFactRow("Dietary Nitrates",
                                "Beet juice, leafy greens, pomegranate. Convert to nitric oxide — the primary vasodilatory mechanism in erection physiology.")
                            NutrientFactRow("Oleic Acid",
                                "Monounsaturated fat in olive oil, avocado, nuts. Associated with higher testosterone in epidemiological studies.")
                        }
                    }
                }
            } else {
                item {
                    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Peak15Colors.Build)
                    }
                }
            }

            item { Spacer(Modifier.height(Peak15Spacing.xl)) }
        }
    }
}

@Composable
private fun MacroOverviewCard(nutrition: NutritionPlan) {
    Surface(
        shape          = MaterialTheme.shapes.extraLarge,
        color          = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier       = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Peak15Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Peak15Spacing.md)
        ) {
            Text("Daily Targets", style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                MacroCell("Calories", "${nutrition.totalCalories}", "kcal", Peak15Colors.Warning)
                MacroCell("Protein",  "${nutrition.proteinGrams}", "g",    Peak15Colors.Primary)
                MacroCell("Carbs",    "${nutrition.carbGrams}",    "g",    Peak15Colors.Build)
                MacroCell("Fat",      "${nutrition.fatGrams}",     "g",    Peak15Colors.Accent)
            }
        }
    }
}

@Composable
private fun MacroCell(label: String, value: String, unit: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(value,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = color)
        Text(unit, style = MaterialTheme.typography.labelSmall, color = color.copy(alpha = 0.7f))
        Text(label, style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun MealDetailCard(meal: Meal) {
    Surface(
        shape          = MaterialTheme.shapes.large,
        color          = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier       = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Peak15Spacing.md),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Restaurant, null,
                        tint = Peak15Colors.Build, modifier = Modifier.size(18.dp))
                    Text(meal.name, style = MaterialTheme.typography.titleSmall)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = MaterialTheme.shapes.extraSmall,
                        color = Peak15Colors.Build.copy(alpha = 0.12f)
                    ) {
                        Text("${meal.proteinGrams}g P",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall, color = Peak15Colors.Build)
                    }
                    Text(meal.time, style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            meal.foods.forEach { food ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .offset(y = 7.dp)
                            .background(Peak15Colors.Build, CircleShape)
                    )
                    Text(food, style = MaterialTheme.typography.bodySmall)
                }
            }
            if (meal.notes.isNotEmpty()) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Text(meal.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun NutrientFactRow(nutrient: String, fact: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(nutrient,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Peak15Colors.Info)
        Text(fact,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
    }
}
