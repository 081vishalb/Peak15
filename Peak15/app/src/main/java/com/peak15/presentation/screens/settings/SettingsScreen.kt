package com.peak15.presentation.screens.settings

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peak15.presentation.components.*
import com.peak15.presentation.theme.*
import com.peak15.presentation.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    vm    : SettingsViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    var showResetDialog    by remember { mutableStateOf(false) }
    var pornFreeStreakInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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

            // ── Program status ─────────────────────────────────────────
            item {
                SettingsSectionHeader("Program")
            }
            item {
                SettingsInfoRow(
                    icon  = Icons.Outlined.CalendarToday,
                    label = "Current Day",
                    value = "Day ${state.settings?.currentDay ?: 1} of 15",
                    color = Peak15Colors.Primary
                )
            }
            item {
                SettingsInfoRow(
                    icon  = Icons.Outlined.Whatshot,
                    label = "Porn-Free Streak",
                    value = "${state.settings?.pornFreeStreak ?: 0} days",
                    color = Peak15Colors.Warning,
                    onEdit = {
                        pornFreeStreakInput = "${state.settings?.pornFreeStreak ?: 0}"
                    }
                )
            }

            // ── Notifications ──────────────────────────────────────────
            item { Spacer(Modifier.height(4.dp)); SettingsSectionHeader("Notifications") }
            item {
                SettingsToggleRow(
                    icon    = Icons.Outlined.Notifications,
                    label   = "Enable Notifications",
                    subtitle= "Morning reminders and supplement alerts",
                    checked = state.settings?.notificationsEnabled ?: true,
                    onToggle= { vm.toggleNotifications(it) }
                )
            }
            item {
                SettingsToggleRow(
                    icon    = Icons.Outlined.WaterDrop,
                    label   = "Water Reminders",
                    subtitle= "Remind you to drink every 2 hours",
                    checked = state.settings?.waterReminderEnabled ?: true,
                    onToggle= { vm.toggleWaterReminder(it) }
                )
            }

            // ── Appearance ─────────────────────────────────────────────
            item { Spacer(Modifier.height(4.dp)); SettingsSectionHeader("Appearance") }
            item {
                SettingsInfoRow(
                    icon  = Icons.Outlined.DarkMode,
                    label = "Theme",
                    value = "Follows system setting",
                    color = Peak15Colors.Primary
                )
            }

            // ── Tracking ───────────────────────────────────────────────
            item { Spacer(Modifier.height(4.dp)); SettingsSectionHeader("Tracking & Goals") }
            item {
                SettingsInfoRow(
                    icon  = Icons.Outlined.Bedtime,
                    label = "Sleep Target",
                    value = "${state.settings?.targetSleepHours?.toInt() ?: 8}h per night",
                    color = Peak15Colors.Build
                )
            }
            item {
                SettingsInfoRow(
                    icon  = Icons.Outlined.MonitorWeight,
                    label = "Body Weight",
                    value = if ((state.settings?.weightKg ?: 0f) > 0)
                                "${state.settings?.weightKg}kg" else "Not set",
                    color = Peak15Colors.Foundation
                )
            }

            // ── Medical warning ────────────────────────────────────────
            item { Spacer(Modifier.height(4.dp)) }
            item {
                Surface(
                    shape    = MaterialTheme.shapes.large,
                    color    = Peak15Colors.Error.copy(alpha = 0.08f),
                    border   = BorderStroke(0.5.dp, Peak15Colors.Error.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(Peak15Spacing.md), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Warning, null, tint = Peak15Colors.Error, modifier = Modifier.size(18.dp))
                            Text("Medical Warning Signs", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold), color = Peak15Colors.Error)
                        }
                        Text(
                            "Seek medical evaluation if you experience: pelvic pain during Kegel exercises, sudden onset erection dysfunction lasting 3+ days, genital numbness, chest pain during exercise, blood in urine or semen, or worsening symptoms rather than improvement.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // ── About ──────────────────────────────────────────────────
            item { Spacer(Modifier.height(4.dp)); SettingsSectionHeader("About") }
            item {
                SettingsInfoRow(Icons.Outlined.Info, "Version", "1.0.0 · Evidence-based program", Peak15Colors.Info)
            }
            item {
                SettingsInfoRow(
                    Icons.Outlined.Biotech,
                    "Evidence Base",
                    "Sports physiology, urology, pelvic physiotherapy",
                    Peak15Colors.Secondary
                )
            }

            // ── Danger zone ────────────────────────────────────────────
            item { Spacer(Modifier.height(4.dp)); SettingsSectionHeader("Data") }
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth().clickable { showResetDialog = true },
                    shape    = MaterialTheme.shapes.large,
                    color    = Peak15Colors.Error.copy(alpha = 0.06f)
                ) {
                    Row(
                        modifier = Modifier.padding(Peak15Spacing.md),
                        horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.RestartAlt, null, tint = Peak15Colors.Error, modifier = Modifier.size(22.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Reset Program", style = MaterialTheme.typography.titleSmall, color = Peak15Colors.Error)
                            Text("Restart from Day 1. Progress will be lost.", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(Icons.Filled.ChevronRight, null, tint = Peak15Colors.Error, modifier = Modifier.size(18.dp))
                    }
                }
            }

            item { Spacer(Modifier.height(Peak15Spacing.xl)) }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Program?") },
            text  = { Text("This will reset your progress to Day 1. Your logged data will be preserved but the current day counter will restart. Are you sure?") },
            confirmButton = {
                TextButton(
                    onClick = { vm.resetProgram(); showResetDialog = false },
                    colors  = ButtonDefaults.textButtonColors(contentColor = Peak15Colors.Error)
                ) { Text("Reset") }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Cancel") }
            }
        )
    }
}

// ─── Settings UI Components ───────────────────────────────────────────────────

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        title,
        style    = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
        color    = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
    )
}

@Composable
private fun SettingsToggleRow(
    icon    : ImageVector,
    label   : String,
    subtitle: String,
    checked : Boolean,
    onToggle: (Boolean) -> Unit
) {
    Surface(
        shape          = MaterialTheme.shapes.large,
        color          = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier       = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(Peak15Spacing.md),
            horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.titleSmall)
                if (subtitle.isNotEmpty()) {
                    Text(subtitle, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Switch(
                checked         = checked,
                onCheckedChange = onToggle,
                colors          = SwitchDefaults.colors(
                    checkedThumbColor      = MaterialTheme.colorScheme.onPrimary,
                    checkedTrackColor      = MaterialTheme.colorScheme.primary,
                    uncheckedTrackColor    = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}

@Composable
private fun SettingsInfoRow(
    icon   : ImageVector,
    label  : String,
    value  : String,
    color  : androidx.compose.ui.graphics.Color,
    onEdit : (() -> Unit)? = null
) {
    Surface(
        shape          = MaterialTheme.shapes.large,
        color          = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier       = Modifier.fillMaxWidth().let {
            if (onEdit != null) it.clickable { onEdit() } else it
        }
    ) {
        Row(
            modifier = Modifier.padding(Peak15Spacing.md),
            horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(38.dp)
                    .background(color.copy(alpha = 0.12f), MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.titleSmall)
                Text(value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (onEdit != null) {
                Icon(Icons.Outlined.Edit, null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp))
            }
        }
    }
}
