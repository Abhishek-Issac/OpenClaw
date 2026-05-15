package com.openclaw.zenith.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclaw.zenith.core.designsystem.theme.OpClawShapes
import com.openclaw.zenith.core.designsystem.theme.OpenClawZenithTheme

/**
 * Animated linear-gradient shimmer used for skeleton loading states (notes
 * fetching, NIM model registry refresh, etc.). Holds the surface clipped to
 * [shape] so feature screens can drop it in for any rectangle.
 */
@Composable
fun ShimmerSurface(
    modifier: Modifier = Modifier,
    shape: Shape = OpClawShapes.medium,
    baseColor: Color = Color.White.copy(alpha = 0.08f),
    highlightColor: Color = Color.White.copy(alpha = 0.24f),
    periodMs: Int = 1_400,
) {
    val transition = rememberInfiniteTransition(label = "Shimmer")
    val translate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = periodMs, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "Shimmer.translate",
    )
    Box(
        modifier =
            modifier
                .clip(shape)
                .background(baseColor)
                .background(
                    brush =
                        Brush.linearGradient(
                            colors = listOf(baseColor, highlightColor, baseColor),
                            start = Offset(x = -200f + translate * 800f, y = 0f),
                            end = Offset(x = translate * 800f, y = 200f),
                        ),
                ),
    )
}

@Preview(showBackground = true)
@Composable
private fun ShimmerSurfacePreview() {
    OpenClawZenithTheme(darkTheme = true) {
        ShimmerSurface(
            modifier =
                Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .height(72.dp),
        )
    }
}
