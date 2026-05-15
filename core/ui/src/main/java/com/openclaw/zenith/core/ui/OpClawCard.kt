package com.openclaw.zenith.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.openclaw.zenith.core.designsystem.theme.OpClawShapes
import com.openclaw.zenith.core.designsystem.theme.OpClawSpacing
import com.openclaw.zenith.core.designsystem.theme.OpenClawZenithTheme

/**
 * OnePlus-styled container surface. Large rounded corners, generous default
 * padding, soft elevation. Intended as the primary content carrier across
 * feature screens (notes, tasks, reminders, mindspace).
 */
@Composable
fun OpClawCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(OpClawSpacing.lg),
    tonalElevation: Dp = 2.dp,
    shadowElevation: Dp = 4.dp,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = OpClawShapes.medium,
        color = containerColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}

@Preview(showBackground = true, name = "OpClawCard / light")
@Composable
private fun OpClawCardPreviewLight() {
    OpenClawZenithTheme(darkTheme = false) {
        OpClawCard(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Quick note — capture a thought, AI will categorise.",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview(showBackground = true, name = "OpClawCard / dark", uiMode = 32)
@Composable
private fun OpClawCardPreviewDark() {
    OpenClawZenithTheme(darkTheme = true) {
        OpClawCard(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Quick note — capture a thought, AI will categorise.",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
