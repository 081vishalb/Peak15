package com.peak15.presentation.screens.recovery

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.peak15.presentation.components.ExpandableCard
import com.peak15.presentation.components.InfoCard
import com.peak15.presentation.theme.Peak15Colors
import com.peak15.presentation.theme.Peak15Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoveryScreen(onBack: () -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recovery Center") },
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
            item {
                InfoCard(
                    "Recovery is when adaptation occurs. Training creates the stimulus — sleep and recovery create the result. Skipping recovery reduces testosterone and increases injury risk."
                )
            }

            // ── Hip Flexor & Pelvic ───────────────────────────────────────
            item {
                ExpandableCard(
                    "Hip Flexor & Pelvic Release",
                    Icons.Outlined.SelfImprovement,
                    Peak15Colors.Secondary,
                    initiallyExpanded = true
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        StretchRow("Kneeling Hip Flexor Stretch", "45s/side", 3,
                            "Kneeling lunge position. Tuck pelvis, lean forward. Critical — tight hip flexors directly compress the pelvic floor.")
                        StretchRow("Pigeon Pose", "60s/side", 2,
                            "External hip rotation stretch. Releases piriformis and deep hip rotators which are fascially connected to the pelvic floor.")
                        StretchRow("Deep Squat Hold", "60s", 3,
                            "Feet shoulder-width, toes out 45°. Supported by doorframe if needed. Opens pelvic floor and entire hip complex.")
                        StretchRow("Adductor Butterfly Stretch", "45s", 3,
                            "Seated, feet together, elbows press knees toward floor. Inner thigh fascia connects directly to pelvic floor.")
                    }
                }
            }

            // ── Thoracic & Neck ───────────────────────────────────────────
            item {
                ExpandableCard(
                    "Thoracic & Neck Mobility",
                    Icons.Outlined.Accessibility,
                    Peak15Colors.Foundation
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        StretchRow("Thoracic Extension over Foam Roller", "30s × 5 positions", 1,
                            "Upper and mid back only — never roll lower back. Improves posture and breathing mechanics immediately.")
                        StretchRow("Chin Tuck", "5s × 10 reps", 3,
                            "Retract chin straight back while keeping eyes level. Strengthens deep neck flexors, reduces forward head posture.")
                        StretchRow("Cervical Lateral Tilt", "30s/side", 3,
                            "Ear gently toward shoulder. Stretches upper trapezius and SCM.")
                        StretchRow("Cat-Cow (Slow)", "10 reps", 3,
                            "On hands and knees. Arch and round spine fully. Mobilises entire thoracolumbar fascia.")
                        StretchRow("World's Greatest Stretch", "5 reps/side", 2,
                            "Lunge, rotate toward front leg, reach arm to sky. Most comprehensive single mobility exercise available.")
                    }
                }
            }

            // ── Neck Strengthening ────────────────────────────────────────
            item {
                ExpandableCard(
                    "Neck Strengthening",
                    Icons.Outlined.FitnessCenter,
                    Peak15Colors.Peak
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        InfoCard(
                            "Neck strength directly affects camera presence, posture, and overall appearance. 10 minutes daily for 15 days produces visible results.",
                            color = Peak15Colors.Peak
                        )
                        StretchRow("Isometric Flexion", "10s × 3", 3,
                            "Hand on forehead. Push forward while resisting with the neck. Zero movement — pure isometric hold.")
                        StretchRow("Isometric Extension", "10s × 3", 3,
                            "Hand on back of head. Push backward while resisting with neck.")
                        StretchRow("Isometric Lateral (both sides)", "10s × 3 each", 3,
                            "Hand on temple. Push sideways while resisting. Builds lateral neck strength for a more defined neck profile.")
                        StretchRow("Slow Neck Circles", "5 each direction", 1,
                            "Slow, full range of motion circles as a warm-up only. Never force range of motion.")
                    }
                }
            }

            // ── Foam Rolling ──────────────────────────────────────────────
            item {
                ExpandableCard(
                    "Foam Rolling Protocol",
                    Icons.Outlined.Sports,
                    Peak15Colors.Build
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        StretchRow("Glutes & Piriformis", "60s/side", 1,
                            "Sit on roller, cross ankle over knee. Roll entire glute. Key for pelvic floor tension release.")
                        StretchRow("Thoracic Spine", "2 min", 1,
                            "Upper and mid back only. Improves posture and breathing mechanics.")
                        StretchRow("Hip Flexors (kneeling position)", "60s/side", 1,
                            "Kneeling on roller, press into hip flexor. Reduces anterior pelvic tilt which improves pelvic floor function.")
                        StretchRow("Calves & Ankles", "60s/side", 1,
                            "Tight calves affect gait, hip alignment, and pelvic position further up the chain.")
                    }
                }
            }

            // ── Sleep Optimisation ────────────────────────────────────────
            item {
                ExpandableCard(
                    "Sleep Optimisation",
                    Icons.Outlined.Bedtime,
                    Peak15Colors.Info
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        SleepTip("Room Temperature",
                            "18–19°C optimal. Core body temperature must drop 1°C to fall and stay asleep. Below 20°C significantly increases deep sleep stage 3 duration.")
                        SleepTip("Screen Elimination",
                            "No screens 60 minutes before bed. Blue light suppresses melatonin secretion from the pineal gland. Use night shift as an absolute minimum.")
                        SleepTip("Consistent Wake Time",
                            "Same wake time every day — including weekends. Circadian rhythm consistency is the single strongest predictor of sleep quality. More important than bedtime.")
                        SleepTip("Legs Up the Wall",
                            "10 minutes before bed, feet elevated against wall. Activates parasympathetic nervous system and improves venous return.")
                        SleepTip("Magnesium Glycinate",
                            "300–400mg before bed. Glycine is an inhibitory neurotransmitter that directly increases slow-wave (deep) sleep duration.")
                        InfoCard(
                            "Testosterone is primarily synthesised during early morning REM sleep. 8+ hours of quality sleep is the highest-leverage testosterone intervention available — more impactful than any supplement in this stack.",
                            color = Peak15Colors.Info
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(Peak15Spacing.xl)) }
        }
    }
}

// ─── Components ───────────────────────────────────────────────────────────────

@Composable
private fun StretchRow(name: String, duration: String, sets: Int, desc: String) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                    shape = MaterialTheme.shapes.extraSmall,
                    color = Peak15Colors.Secondary.copy(alpha = 0.12f)
                ) {
                    Text(duration,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Peak15Colors.Secondary)
                }
                if (sets > 1) {
                    Surface(
                        shape = MaterialTheme.shapes.extraSmall,
                        color = Peak15Colors.Info.copy(alpha = 0.12f)
                    ) {
                        Text("×$sets",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Peak15Colors.Info)
                    }
                }
            }
        }
        Text(desc,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f))
    }
}

@Composable
private fun SleepTip(title: String, desc: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(title,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Peak15Colors.Info)
        Text(desc,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
    }
}
