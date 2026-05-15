package com.openclaw.zenith.core.designsystem.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring

/**
 * Physics-based motion tokens. OnePlus's "Fast and Smooth" identity translates
 * to medium stiffness springs with low-to-medium damping — snappy but not jerky.
 *
 * Use [softSpring] for everyday container/list animations, [snappySpring] for
 * tactile press/release feedback, and [bouncySpring] for hero entrances and the
 * iridescent Gemini blob's idle motion.
 */
object OpClawMotion {
    fun <T> softSpring(): SpringSpec<T> =
        spring(
            dampingRatio = 0.85f,
            stiffness = Spring.StiffnessMedium,
        )

    fun <T> snappySpring(): SpringSpec<T> =
        spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow,
        )

    fun <T> bouncySpring(): SpringSpec<T> =
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        )
}
