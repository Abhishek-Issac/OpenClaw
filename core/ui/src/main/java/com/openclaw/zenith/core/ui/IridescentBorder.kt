package com.openclaw.zenith.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.openclaw.zenith.core.designsystem.theme.GeminiIridescence
import com.openclaw.zenith.core.designsystem.theme.OpClawShapes
import com.openclaw.zenith.core.designsystem.theme.OpenClawZenithTheme
import kotlin.math.cos
import kotlin.math.sin

/**
 * Slowly rotating multi-stop gradient border. Drawn with [drawWithCache] so
 * the per-frame cost is just a small matrix update plus the gradient blit.
 *
 * Pair with [OpClawCard] or any surface to mark AI-generated / AI-suggested
 * content. Stops cycle through the Gemini iridescence palette every [periodMs]
 * milliseconds.
 */
@Composable
fun Modifier.iridescentBorder(
    width: Dp = 2.dp,
    shape: Shape = OpClawShapes.medium,
    periodMs: Int = 6_000,
): Modifier {
    val transition = rememberInfiniteTransition(label = "IridescentBorder")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI).toFloat(),
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = periodMs, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "IridescentBorder.phase",
    )
    return this.drawWithCache {
        val strokePx = width.toPx()
        val outline = shape.createOutline(size, layoutDirection, this)
        val radius = (size.minDimension / 2f).coerceAtLeast(1f)
        val center = Offset(size.width / 2f, size.height / 2f)
        val start = Offset(center.x + cos(phase) * radius, center.y + sin(phase) * radius)
        val end = Offset(center.x - cos(phase) * radius, center.y - sin(phase) * radius)
        val brush = Brush.linearGradient(colors = GeminiIridescence, start = start, end = end)
        onDrawWithContent {
            drawContent()
            drawOutline(
                outline = outline,
                brush = brush,
                style = Stroke(width = strokePx),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IridescentBorderPreview() {
    OpenClawZenithTheme(darkTheme = true) {
        Box(
            modifier =
                Modifier
                    .padding(24.dp)
                    .fillMaxSize()
                    .iridescentBorder(width = 2.dp),
        ) {
            Text(
                text = "AI-suggested",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
