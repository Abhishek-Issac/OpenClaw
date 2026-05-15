package com.openclaw.zenith.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors =
    darkColorScheme(
        primary = OpClawColors.VividRed,
        onPrimary = OpClawColors.SoftWhite,
        primaryContainer = OpClawColors.VividRedMuted,
        onPrimaryContainer = OpClawColors.SoftWhite,
        secondary = OpClawColors.GeminiBlue,
        onSecondary = OpClawColors.SoftWhite,
        tertiary = OpClawColors.GeminiViolet,
        onTertiary = OpClawColors.SoftWhite,
        background = OpClawColors.DeepInk,
        onBackground = OpClawColors.SoftWhite,
        surface = OpClawColors.SlateGray,
        onSurface = OpClawColors.SoftWhite,
        surfaceVariant = OpClawColors.SlateGrayLight,
        onSurfaceVariant = OpClawColors.Mist,
        outline = OpClawColors.SlateGrayLight,
        error = OpClawColors.VividRed,
        onError = OpClawColors.SoftWhite,
    )

private val LightColors =
    lightColorScheme(
        primary = OpClawColors.VividRed,
        onPrimary = OpClawColors.SoftWhite,
        primaryContainer = OpClawColors.VividRedHover,
        onPrimaryContainer = OpClawColors.SoftWhite,
        secondary = OpClawColors.GeminiBlue,
        onSecondary = OpClawColors.SoftWhite,
        tertiary = OpClawColors.GeminiViolet,
        onTertiary = OpClawColors.SoftWhite,
        background = OpClawColors.SoftWhite,
        onBackground = OpClawColors.SlateGray,
        surface = OpClawColors.SoftWhite,
        onSurface = OpClawColors.SlateGray,
        surfaceVariant = OpClawColors.Mist,
        onSurfaceVariant = OpClawColors.SlateGray,
        outline = OpClawColors.SlateGrayLight,
        error = OpClawColors.VividRed,
        onError = OpClawColors.SoftWhite,
    )

/**
 * OpenClaw: Zenith Material 3 theme.
 *
 * Dynamic color is intentionally **not** enabled — brand identity is a
 * non-negotiable per the OnePlus design language pillar.
 */
@Composable
fun OpenClawZenithTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = OpClawTypography,
        shapes = OpClawShapes,
        content = content,
    )
}
