package com.peak15

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.peak15.presentation.navigation.*
import com.peak15.presentation.theme.Peak15Theme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

// ─── Application ─────────────────────────────────────────────────────────────

@HiltAndroidApp
class Peak15Application : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Morning routine reminder
            NotificationChannel(
                CHANNEL_MORNING,
                "Morning Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily morning routine reminder"
                manager.createNotificationChannel(this)
            }

            // Water reminder
            NotificationChannel(
                CHANNEL_WATER,
                "Hydration Reminder",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Drink water reminders throughout the day"
                manager.createNotificationChannel(this)
            }

            // Supplement reminder
            NotificationChannel(
                CHANNEL_SUPPLEMENT,
                "Supplement Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Supplement timing reminders"
                manager.createNotificationChannel(this)
            }

            // Pelvic floor reminder
            NotificationChannel(
                CHANNEL_PELVIC,
                "Training Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Pelvic floor and workout reminders"
                manager.createNotificationChannel(this)
            }
        }
    }

    companion object {
        const val CHANNEL_MORNING    = "peak15_morning"
        const val CHANNEL_WATER      = "peak15_water"
        const val CHANNEL_SUPPLEMENT = "peak15_supplements"
        const val CHANNEL_PELVIC     = "peak15_pelvic"
    }
}

// ─── MainActivity ─────────────────────────────────────────────────────────────

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Peak15Theme {
                Peak15App()
            }
        }
    }
}

// ─── Root App Composable ──────────────────────────────────────────────────────

@Composable
fun Peak15App() {
    val navController = rememberNavController()

    Scaffold(
        modifier     = Modifier.fillMaxSize(),
        bottomBar    = { Peak15BottomBar(navController) }
    ) { innerPadding ->
        Peak15NavHost(navController)
    }
}

// ─── Bottom Navigation Bar ────────────────────────────────────────────────────

@Composable
fun Peak15BottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Only show bottom bar on top-level routes
    val showBottomBar = currentRoute in listOf(
        Routes.DASHBOARD, Routes.ROADMAP, Routes.ANALYTICS, Routes.SETTINGS
    )

    if (!showBottomBar) return

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick  = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo(Routes.DASHBOARD) { saveState = true }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    }
                },
                icon  = {
                    Icon(
                        imageVector = when (item.route) {
                            Routes.DASHBOARD  -> if (selected) Icons.Filled.Home else Icons.Outlined.Home
                            Routes.ROADMAP    -> if (selected) Icons.Filled.CalendarToday else Icons.Outlined.CalendarToday
                            Routes.ANALYTICS  -> if (selected) Icons.Filled.BarChart else Icons.Outlined.BarChart
                            Routes.SETTINGS   -> if (selected) Icons.Filled.Settings else Icons.Outlined.Settings
                            else              -> Icons.Outlined.Home
                        },
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = MaterialTheme.colorScheme.primary,
                    selectedTextColor   = MaterialTheme.colorScheme.primary,
                    indicatorColor      = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}


