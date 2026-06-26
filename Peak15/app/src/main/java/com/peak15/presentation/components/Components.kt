package com.peak15.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peak15.presentation.theme.*

// ─── Metric Card ──────────────────────────────────────────────────────────────

/**
 * A single KPI card used on the Dashboard showing a value with label and icon.
 */
@Composable
fun MetricCard(
    label     : String,
    value     : String,
    unit      : String     = "",
    icon      : ImageVector,
    accentColor: Color     = Peak15Colors.Primary,
    modifier  : Modifier   = Modifier,
    onClick   : (() -> Unit)? = null
) {
    val containerColor = accentColor.copy(alpha = 0.12f)
    val clickableModifier = if (onClick != null)
        modifier.clickable { onClick() } else modifier

    Surface(
        modifier      = clickableModifier,
        shape         = MaterialTheme.shapes.large,
        color         = MaterialTheme.colorScheme.surface,
        tonalElevation= 2.dp
    ) {
        Column(
            modifier = Modifier
                .padding(Peak15Spacing.md)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Peak15Spacing.sm)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text  = buildString { append(value); if (unit.isNotEmpty()) append(" $unit") },
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize   = 28.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text  = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ─── Circular Progress ────────────────────────────────────────────────────────

/**
 * Animated arc-based progress indicator used on Dashboard and day cards.
 */
@Composable
fun CircularProgressIndicatorCustom(
    progress  : Float,            // 0f – 1f
    size      : Dp    = 120.dp,
    strokeWidth: Dp   = 10.dp,
    color     : Color = Peak15Colors.Primary,
    trackColor: Color = color.copy(alpha = 0.15f),
    content   : @Composable BoxScope.() -> Unit = {}
) {
    val animatedProgress by animateFloatAsState(
        targetValue   = progress.coerceIn(0f, 1f),
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label         = "progress"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size)
            .drawBehind {
                val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                val startAngle = -90f
                val radius = (size.toPx() - strokeWidth.toPx()) / 2f
                val topLeft = Offset(strokeWidth.toPx() / 2f, strokeWidth.toPx() / 2f)
                val arcSize = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)

                // Track
                drawArc(
                    color      = trackColor,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter  = false,
                    topLeft    = topLeft,
                    size       = arcSize,
                    style      = stroke
                )
                // Progress
                drawArc(
                    color      = color,
                    startAngle = startAngle,
                    sweepAngle = animatedProgress * 360f,
                    useCenter  = false,
                    topLeft    = topLeft,
                    size       = arcSize,
                    style      = stroke
                )
            }
    ) {
        content()
    }
}

// ─── Progress Bar ─────────────────────────────────────────────────────────────

@Composable
fun AnimatedLinearProgress(
    progress  : Float,
    color     : Color    = Peak15Colors.Primary,
    trackColor: Color    = color.copy(alpha = 0.15f),
    height    : Dp       = 6.dp,
    modifier  : Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue   = progress.coerceIn(0f, 1f),
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label         = "linear_progress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .clip(CircleShape)
                .background(
                    Brush.horizontalGradient(
                        listOf(color.copy(alpha = 0.8f), color)
                    )
                )
        )
    }
}

// ─── Task Completion Row ──────────────────────────────────────────────────────

@Composable
fun TaskRow(
    label     : String,
    completed : Boolean,
    icon      : ImageVector,
    accentColor: Color    = Peak15Colors.Primary,
    onToggle  : () -> Unit,
    modifier  : Modifier  = Modifier
) {
    val bgColor = if (completed)
        accentColor.copy(alpha = 0.10f)
    else
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)

    Surface(
        modifier   = modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape      = MaterialTheme.shapes.medium,
        color      = bgColor,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier            = Modifier.padding(horizontal = Peak15Spacing.md, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.md),
            verticalAlignment   = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (completed) accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text  = label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (completed)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
                fontWeight = if (completed) FontWeight.Medium else FontWeight.Normal
            )
            AnimatedContent(
                targetState = completed,
                transitionSpec = { scaleIn() + fadeIn() togetherWith scaleOut() + fadeOut() },
                label = "check_anim"
            ) { done ->
                if (done) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Completed",
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = "Not completed",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

// ─── Section Header ───────────────────────────────────────────────────────────

@Composable
fun SectionHeader(
    title    : String,
    subtitle : String      = "",
    action   : String      = "",
    onAction : (() -> Unit)? = null,
    modifier : Modifier    = Modifier
) {
    Row(
        modifier              = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text  = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (subtitle.isNotEmpty()) {
                Text(
                    text  = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (action.isNotEmpty() && onAction != null) {
            TextButton(onClick = onAction) {
                Text(text = action, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

// ─── Phase Badge ─────────────────────────────────────────────────────────────

@Composable
fun PhaseBadge(day: Int, modifier: Modifier = Modifier) {
    val (label, color) = when {
        day <= 5  -> "Foundation" to Peak15Colors.Foundation
        day <= 10 -> "Build"      to Peak15Colors.Build
        else      -> "Peak"       to Peak15Colors.Peak
    }
    Surface(
        modifier = modifier,
        shape    = CircleShape,
        color    = color.copy(alpha = 0.15f)
    ) {
        Text(
            text     = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style    = MaterialTheme.typography.labelSmall,
            color    = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ─── Water Button ─────────────────────────────────────────────────────────────

@Composable
fun WaterAddButton(
    label  : String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick  = onClick,
        modifier = modifier,
        shape    = MaterialTheme.shapes.medium,
        border   = BorderStroke(1.dp, Peak15Colors.Primary.copy(alpha = 0.4f)),
        colors   = ButtonDefaults.outlinedButtonColors(
            containerColor = Peak15Colors.Primary.copy(alpha = 0.06f)
        )
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Peak15Colors.Primary
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text  = label,
            style = MaterialTheme.typography.labelMedium,
            color = Peak15Colors.Primary
        )
    }
}

// ─── Score Slider ─────────────────────────────────────────────────────────────

@Composable
fun ScoreSlider(
    label    : String,
    value    : Int,
    onValue  : (Int) -> Unit,
    color    : Color    = Peak15Colors.Primary,
    modifier : Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                "$value / 10",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = color
            )
        }
        Slider(
            value         = value.toFloat(),
            onValueChange = { onValue(it.toInt()) },
            valueRange    = 1f..10f,
            steps         = 8,
            colors        = SliderDefaults.colors(
                thumbColor          = color,
                activeTrackColor    = color,
                inactiveTrackColor  = color.copy(alpha = 0.2f)
            )
        )
    }
}

// ─── Info Card ────────────────────────────────────────────────────────────────

@Composable
fun InfoCard(
    text    : String,
    icon    : ImageVector = Icons.Outlined.Info,
    color   : Color       = Peak15Colors.Info,
    modifier: Modifier    = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape    = MaterialTheme.shapes.medium,
        color    = color.copy(alpha = 0.10f)
    ) {
        Row(
            modifier  = Modifier.padding(Peak15Spacing.md),
            horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.sm),
            verticalAlignment = Alignment.Top
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(18.dp).padding(top = 1.dp))
            Text(
                text  = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                lineHeight = 18.sp
            )
        }
    }
}

// ─── Top App Bar ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Peak15TopBar(
    title      : String,
    subtitle   : String    = "",
    showBack   : Boolean   = false,
    onBack     : () -> Unit = {},
    actions    : @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text  = title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        text  = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = actions,
        colors  = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

// ─── Expandable Card ─────────────────────────────────────────────────────────

@Composable
fun ExpandableCard(
    title    : String,
    icon     : ImageVector,
    accentColor: Color = Peak15Colors.Primary,
    modifier : Modifier = Modifier,
    initiallyExpanded: Boolean = false,
    content  : @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    Surface(
        modifier   = modifier.fillMaxWidth(),
        shape      = MaterialTheme.shapes.large,
        color      = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(Peak15Spacing.md),
                horizontalArrangement = Arrangement.spacedBy(Peak15Spacing.sm),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(accentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = accentColor, modifier = Modifier.size(18.dp))
                }
                Text(
                    text     = title,
                    style    = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                val rotation by animateFloatAsState(
                    targetValue   = if (expanded) 180f else 0f,
                    animationSpec = tween(250),
                    label         = "arrow_rotation"
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.rotate(rotation)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter   = expandVertically(tween(250)) + fadeIn(tween(250)),
                exit    = shrinkVertically(tween(200)) + fadeOut(tween(200))
            ) {
                Column(
                    modifier = Modifier.padding(
                        start   = Peak15Spacing.md,
                        end     = Peak15Spacing.md,
                        bottom  = Peak15Spacing.md
                    ),
                    verticalArrangement = Arrangement.spacedBy(Peak15Spacing.sm)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    Spacer(Modifier.height(4.dp))
                    content()
                }
            }
        }
    }
}

// ─── Streak Badge ─────────────────────────────────────────────────────────────

@Composable
fun StreakBadge(streak: Int, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape    = MaterialTheme.shapes.medium,
        color    = Peak15Colors.Warning.copy(alpha = 0.15f)
    ) {
        Row(
            modifier  = Modifier.padding(horizontal = Peak15Spacing.sm, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text("🔥", fontSize = 16.sp)
            Text(
                text  = "$streak day streak",
                style = MaterialTheme.typography.labelMedium,
                color = Peak15Colors.Warning,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
