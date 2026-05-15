package com.openclaw.zenith.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * OnePlus-inspired brand palette for OpenClaw: Zenith.
 *
 * Tokens are exposed as named [Color] constants so feature modules pull from the
 * design system rather than redefining hex values inline.
 */
object OpClawColors {
    // Brand cores
    val SlateGray: Color = Color(0xFF2A2D31)
    val VividRed: Color = Color(0xFFEB0029)
    val SoftWhite: Color = Color(0xFFF5F5F7)
    val DeepInk: Color = Color(0xFF111316)

    // Supporting neutrals
    val SlateGrayLight: Color = Color(0xFF3A3D42)
    val SlateGrayDark: Color = Color(0xFF1A1D21)
    val Mist: Color = Color(0xFFE6E6EA)
    val Onyx: Color = Color(0xFF0A0B0D)

    // Accents (used for state, charts, focus rings)
    val VividRedHover: Color = Color(0xFFFF3355)
    val VividRedMuted: Color = Color(0xFFA8001E)
    val GeminiBlue: Color = Color(0xFF4285F4)
    val GeminiTeal: Color = Color(0xFF34A2A2)
    val GeminiViolet: Color = Color(0xFF9B59FF)
    val GeminiRose: Color = Color(0xFFFF6F91)

    // Semantic
    val Success: Color = Color(0xFF2EC27E)
    val Warning: Color = Color(0xFFE5A50A)
    val Danger: Color = VividRed
}

/**
 * Iridescent gradient stops inspired by Gemini's halo. Used by overlay surfaces,
 * loading indicators, and the OpenClaw blob.
 */
val GeminiIridescence: List<Color> =
    listOf(
        OpClawColors.GeminiBlue,
        OpClawColors.GeminiTeal,
        OpClawColors.GeminiViolet,
        OpClawColors.GeminiRose,
        OpClawColors.GeminiBlue,
    )
