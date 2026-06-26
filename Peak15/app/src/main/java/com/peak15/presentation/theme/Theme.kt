package com.peak15.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Color Palette ─────────────────────────────────────────────────────────────

object Peak15Colors {
    // Brand
    val Primary       = Color(0xFF4F8EF7)   // confident blue
    val PrimaryDark   = Color(0xFF2E6FD8)
    val Secondary     = Color(0xFF32C88A)   // vitality green
    val Accent        = Color(0xFFFF6B35)   // energy orange

    // Phase colours
    val Foundation    = Color(0xFF4F8EF7)   // blue
    val Build         = Color(0xFF32C88A)   // green
    val Peak          = Color(0xFFFF9500)   // amber/gold

    // Semantic
    val Success       = Color(0xFF34C759)
    val Warning       = Color(0xFFFF9500)
    val Error         = Color(0xFFFF3B30)
    val Info          = Color(0xFF007AFF)

    // Metric cards
    val CardBlue      = Color(0xFF1E3A5F)
    val CardGreen     = Color(0xFF1A3D2F)
    val CardOrange    = Color(0xFF3D2210)
    val CardPurple    = Color(0xFF2D1B5E)

    // Surfaces – dark
    val SurfaceDark   = Color(0xFF0D0D0D)
    val Surface1Dark  = Color(0xFF141414)
    val Surface2Dark  = Color(0xFF1C1C1E)
    val Surface3Dark  = Color(0xFF2C2C2E)
    val BorderDark    = Color(0xFF2C2C2E)

    // Surfaces – light
    val SurfaceLight  = Color(0xFFF2F2F7)
    val Surface1Light = Color(0xFFFFFFFF)
    val Surface2Light = Color(0xFFF8F8FA)
    val Surface3Light = Color(0xFFEEEEF4)
    val BorderLight   = Color(0xFFE0E0E5)
}

// ─── Dark Color Scheme ─────────────────────────────────────────────────────────

private val DarkColorScheme = darkColorScheme(
    primary            = Peak15Colors.Primary,
    onPrimary          = Color.White,
    primaryContainer   = Color(0xFF1A2F55),
    onPrimaryContainer = Color(0xFFADCBFF),
    secondary          = Peak15Colors.Secondary,
    onSecondary        = Color.Black,
    secondaryContainer = Color(0xFF0E2E1F),
    onSecondaryContainer = Color(0xFF9EEEC5),
    tertiary           = Peak15Colors.Accent,
    onTertiary         = Color.Black,
    background         = Peak15Colors.SurfaceDark,
    onBackground       = Color(0xFFF5F5F5),
    surface            = Peak15Colors.Surface1Dark,
    onSurface          = Color(0xFFF0F0F0),
    surfaceVariant     = Peak15Colors.Surface2Dark,
    onSurfaceVariant   = Color(0xFFAAAAAA),
    outline            = Peak15Colors.BorderDark,
    error              = Peak15Colors.Error,
    onError            = Color.White
)

// ─── Light Color Scheme ───────────────────────────────────────────────────────

private val LightColorScheme = lightColorScheme(
    primary            = Peak15Colors.PrimaryDark,
    onPrimary          = Color.White,
    primaryContainer   = Color(0xFFDCEAFF),
    onPrimaryContainer = Color(0xFF001D45),
    secondary          = Color(0xFF1A7A50),
    onSecondary        = Color.White,
    secondaryContainer = Color(0xFFD0F4E4),
    onSecondaryContainer = Color(0xFF00391F),
    tertiary           = Color(0xFFBF4F00),
    onTertiary         = Color.White,
    background         = Peak15Colors.SurfaceLight,
    onBackground       = Color(0xFF1A1A1A),
    surface            = Peak15Colors.Surface1Light,
    onSurface          = Color(0xFF1A1A1A),
    surfaceVariant     = Peak15Colors.Surface2Light,
    onSurfaceVariant   = Color(0xFF555555),
    outline            = Peak15Colors.BorderLight,
    error              = Peak15Colors.Error,
    onError            = Color.White
)

// ─── Typography ────────────────────────────────────────────────────────────────

val Peak15Typography = Typography(
    // Display – Hero numbers on dashboard
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize   = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize   = 45.sp,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize   = 36.sp,
        lineHeight = 44.sp
    ),
    // Headlines – Section titles
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize   = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize   = 28.sp,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize   = 24.sp,
        lineHeight = 32.sp
    ),
    // Titles – Card headings
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize   = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    // Body
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 12.sp,
        lineHeight = 16.sp
    ),
    // Labels
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

// ─── Spacing ──────────────────────────────────────────────────────────────────

object Peak15Spacing {
    val xs   : Dp = 4.dp
    val sm   : Dp = 8.dp
    val md   : Dp = 16.dp
    val lg   : Dp = 24.dp
    val xl   : Dp = 32.dp
    val xxl  : Dp = 48.dp
    val xxxl : Dp = 64.dp
}

// ─── Shape ────────────────────────────────────────────────────────────────────

val Peak15Shapes = Shapes(
    extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
    small      = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    medium     = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    large      = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
)

// ─── Theme Composable ─────────────────────────────────────────────────────────

@Composable
fun Peak15Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Peak15Typography,
        shapes      = Peak15Shapes,
        content     = content
    )
}

// ─── Phase Color Helper ───────────────────────────────────────────────────────

fun phaseColor(day: Int): Color = when {
    day <= 5  -> Peak15Colors.Foundation
    day <= 10 -> Peak15Colors.Build
    else      -> Peak15Colors.Peak
}

fun phaseLabel(day: Int): String = when {
    day <= 5  -> "Foundation"
    day <= 10 -> "Build"
    else      -> "Peak"
}
