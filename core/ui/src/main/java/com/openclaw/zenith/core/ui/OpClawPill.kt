package com.openclaw.zenith.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclaw.zenith.core.designsystem.theme.OpClawMotion
import com.openclaw.zenith.core.designsystem.theme.OpClawSpacing
import com.openclaw.zenith.core.designsystem.theme.OpenClawZenithTheme
import com.openclaw.zenith.core.designsystem.theme.PillShape

/**
 * Pill / chip primitive. Used for filters, model selectors, status badges, and
 * the quick-action row in OpenClaw's overlay.
 */
@Composable
fun OpClawPill(
    label: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    val backgroundTarget =
        when {
            !enabled -> MaterialTheme.colorScheme.surfaceVariant
            selected -> MaterialTheme.colorScheme.primary
            else -> Color.Transparent
        }
    val foregroundTarget =
        when {
            !enabled -> MaterialTheme.colorScheme.onSurfaceVariant
            selected -> MaterialTheme.colorScheme.onPrimary
            else -> MaterialTheme.colorScheme.onSurface
        }
    val background by animateColorAsState(
        targetValue = backgroundTarget,
        animationSpec = OpClawMotion.softSpring(),
        label = "OpClawPill.background",
    )
    val foreground by animateColorAsState(
        targetValue = foregroundTarget,
        animationSpec = OpClawMotion.softSpring(),
        label = "OpClawPill.foreground",
    )
    val borderColor =
        if (selected) Color.Transparent else MaterialTheme.colorScheme.outline
    Text(
        text = label,
        color = foreground,
        style = MaterialTheme.typography.labelLarge,
        modifier =
            modifier
                .clip(PillShape)
                .background(background, PillShape)
                .border(width = 1.dp, color = borderColor, shape = PillShape)
                .clickable(enabled = enabled, onClick = onClick)
                .padding(PaddingValues(horizontal = OpClawSpacing.lg, vertical = OpClawSpacing.sm)),
    )
}

@Preview(showBackground = true)
@Composable
private fun OpClawPillPreview() {
    OpenClawZenithTheme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OpClawPill(label = "Gemini-2.5-pro", selected = true)
            OpClawPill(label = "NIM • llama-3.1-70b", selected = false)
            OpClawPill(label = "Disabled", enabled = false)
        }
    }
}
