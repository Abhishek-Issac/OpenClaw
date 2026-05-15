package com.openclaw.zenith.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclaw.zenith.core.designsystem.theme.GeminiIridescence
import com.openclaw.zenith.core.designsystem.theme.OpenClawZenithTheme
import kotlin.math.cos
import kotlin.math.sin

/**
 * Animated iridescent blob — Gemini's signature "thinking" indicator. Two
 * counter-rotating radial gradients composited together give the rolling
 * pearl effect without a video texture.
 *
 * Drop wherever the AI is doing background work (model fetch, generation
 * streaming, focus-session insight crunching).
 */
@Composable
fun GeminiBlob(
    modifier: Modifier = Modifier,
    periodMs: Int = 4_200,
) {
    val transition = rememberInfiniteTransition(label = "GeminiBlob")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI).toFloat(),
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = periodMs, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "GeminiBlob.phase",
    )
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val r = (size.minDimension / 2f)
        val cx = w / 2f
        val cy = h / 2f
        val driftA = Offset(cx + cos(phase) * r * 0.18f, cy + sin(phase) * r * 0.18f)
        val driftB = Offset(cx - cos(phase * 0.7f) * r * 0.22f, cy - sin(phase * 1.3f) * r * 0.22f)
        drawCircle(
            brush = Brush.radialGradient(colors = GeminiIridescence, center = driftA, radius = r),
            radius = r,
            center = Offset(cx, cy),
        )
        drawCircle(
            brush =
                Brush.radialGradient(
                    colors = GeminiIridescence.asReversed(),
                    center = driftB,
                    radius = r * 0.85f,
                ),
            radius = r * 0.85f,
            center = Offset(cx, cy),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GeminiBlobPreview() {
    OpenClawZenithTheme(darkTheme = true) {
        Box(
            modifier =
                Modifier
                    .padding(24.dp)
                    .fillMaxSize(),
        ) {
            GeminiBlob(modifier = Modifier.size(144.dp))
        }
    }
}
