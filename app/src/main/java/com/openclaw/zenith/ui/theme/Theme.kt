package com.openclaw.zenith.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// OnePlus-inspired palette: Slate Gray, Vivid Red, Soft White.
private val SlateGray = Color(0xFF2A2D31)
private val VividRed = Color(0xFFEB0029)
private val SoftWhite = Color(0xFFF5F5F7)
private val DeepInk = Color(0xFF111316)

private val DarkColors =
    darkColorScheme(
        primary = VividRed,
        onPrimary = SoftWhite,
        background = DeepInk,
        onBackground = SoftWhite,
        surface = SlateGray,
        onSurface = SoftWhite,
    )

private val LightColors =
    lightColorScheme(
        primary = VividRed,
        onPrimary = SoftWhite,
        background = SoftWhite,
        onBackground = SlateGray,
        surface = SoftWhite,
        onSurface = SlateGray,
    )

@Composable
fun OpenClawZenithTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, content = content)
}
