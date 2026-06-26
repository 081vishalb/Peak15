package com.peak15.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.peak15.presentation.screens.analytics.AnalyticsScreen
import com.peak15.presentation.screens.cardio.CardioScreen
import com.peak15.presentation.screens.confidence.ConfidenceScreen
import com.peak15.presentation.screens.dashboard.DashboardScreen
import com.peak15.presentation.screens.fitness.FitnessScreen
import com.peak15.presentation.screens.fitness.WorkoutDetailScreen
import com.peak15.presentation.screens.nutrition.NutritionScreen
import com.peak15.presentation.screens.pelvicfloor.PelvicFloorScreen
import com.peak15.presentation.screens.recovery.RecoveryScreen
import com.peak15.presentation.screens.roadmap.RoadmapScreen
import com.peak15.presentation.screens.roadmap.DayDetailScreen
import com.peak15.presentation.screens.settings.SettingsScreen

// ─── Route constants ──────────────────────────────────────────────────────────

object Routes {
    const val DASHBOARD    = "dashboard"
    const val ROADMAP      = "roadmap"
    const val DAY_DETAIL   = "day_detail/{day}"
    const val PELVIC_FLOOR = "pelvic_floor"
    const val FITNESS      = "fitness"
    const val WORKOUT_DETAIL = "workout_detail/{day}"
    const val CARDIO       = "cardio"
    const val NUTRITION    = "nutrition"
    const val CONFIDENCE   = "confidence"
    const val RECOVERY     = "recovery"
    const val ANALYTICS    = "analytics"
    const val SETTINGS     = "settings"

    fun dayDetail(day: Int) = "day_detail/$day"
    fun workoutDetail(day: Int) = "workout_detail/$day"
}

// ─── Bottom Nav Items ─────────────────────────────────────────────────────────

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: String   // Material icon name string for reference
) {
    object Dashboard   : BottomNavItem(Routes.DASHBOARD,    "Today",    "home")
    object Roadmap     : BottomNavItem(Routes.ROADMAP,      "Program",  "calendar_today")
    object Analytics   : BottomNavItem(Routes.ANALYTICS,   "Progress", "bar_chart")
    object Settings    : BottomNavItem(Routes.SETTINGS,    "Settings", "settings")
}

val bottomNavItems = listOf(
    BottomNavItem.Dashboard,
    BottomNavItem.Roadmap,
    BottomNavItem.Analytics,
    BottomNavItem.Settings
)

// ─── Nav Host ─────────────────────────────────────────────────────────────────

@Composable
fun Peak15NavHost(navController: NavHostController) {
    NavHost(
        navController    = navController,
        startDestination = Routes.DASHBOARD,
        enterTransition  = {
            fadeIn(tween(220)) + slideInHorizontally(tween(220)) { it / 4 }
        },
        exitTransition   = {
            fadeOut(tween(180)) + slideOutHorizontally(tween(180)) { -it / 4 }
        },
        popEnterTransition = {
            fadeIn(tween(220)) + slideInHorizontally(tween(220)) { -it / 4 }
        },
        popExitTransition  = {
            fadeOut(tween(180)) + slideOutHorizontally(tween(180)) { it / 4 }
        }
    ) {
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onNavigate = { navController.navigate(it) }
            )
        }

        composable(Routes.ROADMAP) {
            RoadmapScreen(
                onDayClick = { day -> navController.navigate(Routes.dayDetail(day)) }
            )
        }

        composable(
            Routes.DAY_DETAIL,
            arguments = listOf(navArgument("day") { type = NavType.IntType })
        ) { back ->
            DayDetailScreen(
                day         = back.arguments?.getInt("day") ?: 1,
                onBack      = { navController.popBackStack() },
                onWorkout   = { day -> navController.navigate(Routes.workoutDetail(day)) },
                onPelvic    = { navController.navigate(Routes.PELVIC_FLOOR) },
                onCardio    = { navController.navigate(Routes.CARDIO) },
                onNutrition = { navController.navigate(Routes.NUTRITION) },
                onConfidence= { navController.navigate(Routes.CONFIDENCE) },
                onRecovery  = { navController.navigate(Routes.RECOVERY) }
            )
        }

        composable(Routes.PELVIC_FLOOR) {
            PelvicFloorScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.FITNESS) {
            FitnessScreen(
                onWorkoutClick = { day -> navController.navigate(Routes.workoutDetail(day)) },
                onBack         = { navController.popBackStack() }
            )
        }

        composable(
            Routes.WORKOUT_DETAIL,
            arguments = listOf(navArgument("day") { type = NavType.IntType })
        ) { back ->
            WorkoutDetailScreen(
                day    = back.arguments?.getInt("day") ?: 1,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.CARDIO) {
            CardioScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.NUTRITION) {
            NutritionScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.CONFIDENCE) {
            ConfidenceScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.RECOVERY) {
            RecoveryScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.ANALYTICS) {
            AnalyticsScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
