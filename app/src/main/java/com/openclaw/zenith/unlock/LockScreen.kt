package com.openclaw.zenith.unlock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.openclaw.zenith.core.designsystem.theme.OpClawSpacing
import com.openclaw.zenith.core.ui.GeminiBlob

@Composable
fun LockScreen(
    state: LockState,
    contentPadding: PaddingValues,
    onUnlockClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = OpClawSpacing.xl, vertical = OpClawSpacing.xl),
        verticalArrangement =
            Arrangement.spacedBy(
                space = OpClawSpacing.lg,
                alignment = Alignment.CenterVertically,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GeminiBlob(modifier = Modifier.size(128.dp))
        Text(text = "OpenClaw is locked", style = MaterialTheme.typography.headlineMedium)
        Text(
            text =
                "Your notes, tasks, and reminders are encrypted at rest. " +
                    "Authenticate to unlock.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        )
        if (state is LockState.Error) {
            Text(
                text = state.message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
        Button(
            onClick = onUnlockClick,
            enabled = state !is LockState.Authenticating,
        ) {
            Text(
                text =
                    when (state) {
                        LockState.Authenticating -> "Authenticating…"
                        else -> "Unlock with biometrics"
                    },
            )
        }
    }
}
