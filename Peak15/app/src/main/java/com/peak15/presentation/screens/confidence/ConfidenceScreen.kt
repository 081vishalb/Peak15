package com.peak15.presentation.screens.confidence

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.peak15.presentation.components.ExpandableCard
import com.peak15.presentation.components.InfoCard
import com.peak15.presentation.components.SectionHeader
import com.peak15.presentation.theme.Peak15Colors
import com.peak15.presentation.theme.Peak15Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfidenceScreen(onBack: () -> Unit) {

    val challenges = remember { confidenceChallenges() }
    val completed  = remember { mutableStateMapOf<String, Boolean>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confidence Training") },
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
                    "Confidence is a trainable skill, not a fixed trait. These evidence-based exercises are drawn from sports psychology, CBT, and somatic therapy.",
                    icon = Icons.Outlined.Psychology
                )
            }

            item {
                SectionHeader(
                    "Daily Challenges",
                    subtitle = "${completed.count { it.value }}/${challenges.size} completed today"
                )
            }

            challenges.forEach { challenge ->
                item {
                    ConfidenceChallengeCard(
                        challenge = challenge,
                        isDone    = completed[challenge.id] == true,
                        onToggle  = {
                            completed[challenge.id] = !(completed[challenge.id] ?: false)
                        }
                    )
                }
            }

            item {
                ExpandableCard("Breathing Exercises", Icons.Outlined.Air, Peak15Colors.Info) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        BreathingRow("Box Breathing (4-4-4-4)",
                            "Inhale 4s → Hold 4s → Exhale 4s → Hold 4s. Repeat 4–6 rounds. Used by Navy SEALs for pre-performance stress regulation.",
                            Peak15Colors.Primary)
                        BreathingRow("4-7-8 Breath",
                            "Inhale 4s → Hold 7s → Exhale 8s. Powerful parasympathetic activator. Use before sleep and before any performance.",
                            Peak15Colors.Build)
                        BreathingRow("Physiological Sigh",
                            "Double inhale through nose (sniff, then sniff more) then one long exhale through mouth. Proven to reduce stress in under 60 seconds. Stanford 2023.",
                            Peak15Colors.Secondary)
                        BreathingRow("Wim Hof Method",
                            "30 deep power breaths → exhale and hold → recovery inhale and hold 15s. Repeat 3 rounds. Increases adrenaline, reduces inflammation, builds mental resilience.",
                            Peak15Colors.Accent)
                    }
                }
            }

            item {
                ExpandableCard("Posture Training", Icons.Outlined.AccessibilityNew, Peak15Colors.Peak) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        PostureRow("Wall Stand",
                            "Heels, glutes, upper back, and back of head all touching wall. Hold 60s. Walk away maintaining the position. Do this daily.")
                        PostureRow("Power Pose",
                            "Feet shoulder-width, hands on hips or arms raised in a V. Hold 2 minutes. Research shows measurable cortisol reduction and confidence increase.")
                        PostureRow("Chin Tuck",
                            "Gently retract chin straight back while keeping eyes level (creates double chin). Fixes forward head posture. Hold 5s × 10 reps.")
                        PostureRow("Thoracic Extension",
                            "Foam roller placed across upper back. Extend over it slowly. Counteracts desk posture and phone slouch. Hold 30s, move 2 inches, repeat.")
                    }
                }
            }

            item {
                ExpandableCard("Eye Contact & Social Practice", Icons.Outlined.Visibility, Peak15Colors.Foundation) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Eye contact protocol:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                        Text("Hold eye contact for 3–4 seconds before naturally looking away. Smile slightly. Practice with every person you interact with today.",
                            style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.height(4.dp))
                        Text("Stranger conversation drill:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                        Text("Have one genuine 10-minute conversation with a stranger. Ask one real question and listen fully. This is systematic desensitisation — anxiety reduces with repeated exposure.",
                            style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.height(4.dp))
                        InfoCard("Mirror affirmations: stand in front of a mirror and speak 5 identity-level statements aloud — 'I am calm. I am strong. I am in control. I perform at my best. I am present.' Research on pre-performance self-talk shows measurable performance gains.")
                    }
                }
            }

            item {
                ExpandableCard("Visualization Protocol", Icons.Outlined.Visibility, Peak15Colors.Secondary) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Step-by-step (10 minutes):",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                        listOf(
                            "1. Find a quiet space. Lie down or sit comfortably.",
                            "2. Close your eyes. Take 3 slow breaths.",
                            "3. Vividly picture yourself calm, confident, physically capable.",
                            "4. Engage all senses — feel the environment, hear sounds.",
                            "5. See yourself performing at your best with full control.",
                            "6. End with 3 slow breaths. Open your eyes.",
                            "Science: the brain activates the same motor pathways during vivid mental rehearsal as during actual performance."
                        ).forEach { step ->
                            Text(step,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f))
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(Peak15Spacing.xl)) }
        }
    }
}

// ─── Data ────────────────────────────────────────────────────────────────────

private data class ConfidenceChallengeData(
    val id      : String,
    val title   : String,
    val description: String,
    val category: String,
    val duration: String,
    val icon    : ImageVector,
    val color   : Color
)

private fun confidenceChallenges() = listOf(
    ConfidenceChallengeData("c1", "Mirror Affirmations",
        "Stand in front of mirror, speak 5 identity statements aloud with conviction.",
        "Affirmation", "3 min", Icons.Outlined.Person, Peak15Colors.Primary),
    ConfidenceChallengeData("c2", "Stranger Conversation",
        "Have a genuine 10-minute conversation with a stranger today.",
        "Social", "10 min", Icons.Outlined.Chat, Peak15Colors.Build),
    ConfidenceChallengeData("c3", "Power Pose",
        "Stand in power pose for 2 full minutes. Time it — don't cheat.",
        "Posture", "2 min", Icons.Outlined.AccessibilityNew, Peak15Colors.Peak),
    ConfidenceChallengeData("c4", "Performance Visualization",
        "Eyes-closed visualization of performing with full confidence and control.",
        "Visualization", "10 min", Icons.Outlined.Visibility, Peak15Colors.Secondary),
    ConfidenceChallengeData("c5", "Gratitude Journal",
        "Write 10 specific gratitude items — no vague ones. Reduces cortisol measurably.",
        "Mental", "5 min", Icons.Outlined.Edit, Peak15Colors.Warning),
    ConfidenceChallengeData("c6", "4-7-8 Breathing",
        "Complete 4 full cycles of 4-7-8 breathing. Best done before bed.",
        "Breathwork", "4 min", Icons.Outlined.Air, Peak15Colors.Info)
)

// ─── Components ───────────────────────────────────────────────────────────────

@Composable
private fun ConfidenceChallengeCard(
    challenge: ConfidenceChallengeData,
    isDone   : Boolean,
    onToggle : () -> Unit
) {
    Surface(
        modifier       = Modifier.fillMaxWidth().clickable { onToggle() },
        shape          = MaterialTheme.shapes.large,
        color          = if (isDone) challenge.color.copy(alpha = 0.08f)
                         else MaterialTheme.colorScheme.surface,
        tonalElevation = if (isDone) 0.dp else 1.dp,
        border         = if (isDone) BorderStroke(1.dp, challenge.color.copy(alpha = 0.4f)) else null
    ) {
        Row(
            modifier = Modifier.padding(Peak15Spacing.md),
            horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = challenge.color.copy(alpha = 0.12f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(challenge.icon, null,
                        tint = challenge.color, modifier = Modifier.size(22.dp))
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(challenge.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isDone) challenge.color
                            else MaterialTheme.colorScheme.onSurface)
                Text(challenge.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${challenge.category} · ${challenge.duration}",
                    style = MaterialTheme.typography.labelSmall,
                    color = challenge.color)
            }
            Icon(
                if (isDone) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                contentDescription = if (isDone) "Completed" else "Not completed",
                tint = if (isDone) challenge.color
                       else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun BreathingRow(name: String, desc: String, color: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(name,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = color)
        Text(desc,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
    }
}

@Composable
private fun PostureRow(name: String, desc: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(name,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Peak15Colors.Peak)
        Text(desc,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
    }
}
