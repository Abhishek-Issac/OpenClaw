package com.openclaw.zenith.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclaw.zenith.core.designsystem.theme.OpClawSpacing
import com.openclaw.zenith.core.designsystem.theme.OpenClawZenithTheme
import com.openclaw.zenith.core.ui.GeminiBlob
import com.openclaw.zenith.core.ui.OpClawCard
import com.openclaw.zenith.core.ui.OpClawPill
import com.openclaw.zenith.core.ui.ShimmerSurface
import com.openclaw.zenith.core.ui.iridescentBorder

/**
 * Phase 1 acceptance criterion: a single screen that renders every primitive
 * from `:core:ui` against the OnePlus theme. Doubles as a live preview surface
 * for designers and a smoke test for the design system.
 */
@Composable
fun ComponentGalleryScreen(contentPadding: PaddingValues) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = OpClawSpacing.lg, vertical = OpClawSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(OpClawSpacing.lg),
    ) {
        Header()
        CardsSection()
        PillsSection()
        IridescentSection()
        ShimmerSection()
        BlobSection()
    }
}

@Composable
private fun Header() {
    Column(verticalArrangement = Arrangement.spacedBy(OpClawSpacing.xs)) {
        Text(
            text = "OpenClaw: Zenith",
            style = MaterialTheme.typography.displaySmall,
        )
        Text(
            text = "Component gallery — Phase 1 design system",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        )
    }
}

@Composable
private fun CardsSection() {
    SectionTitle("Cards")
    OpClawCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(OpClawSpacing.xs)) {
            Text("MindSpace", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Cognitive-load aware focus sessions, powered by usage signals.",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
    OpClawCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(OpClawSpacing.xs)) {
            Text("Notes", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Voice-captured notes auto-categorised. Action items roll into tasks.",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun PillsSection() {
    SectionTitle("Pills / model selector")
    var selected by remember { mutableStateOf("Gemini-2.5-pro") }
    Row(horizontalArrangement = Arrangement.spacedBy(OpClawSpacing.sm)) {
        listOf("Gemini-2.5-pro", "NIM • llama-3.1-70b", "NIM • mixtral-8x22b").forEach { id ->
            OpClawPill(
                label = id,
                selected = selected == id,
                onClick = { selected = id },
            )
        }
    }
}

@Composable
private fun IridescentSection() {
    SectionTitle("Iridescent border (AI-suggested)")
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(96.dp)
                .iridescentBorder(width = 2.dp),
    ) {
        Text(
            text = "Suggested: reply to Sarah about Friday's review",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(OpClawSpacing.lg),
        )
    }
}

@Composable
private fun ShimmerSection() {
    SectionTitle("Shimmer skeleton")
    ShimmerSurface(modifier = Modifier.fillMaxWidth().height(56.dp))
    ShimmerSurface(modifier = Modifier.fillMaxWidth().height(56.dp))
}

@Composable
private fun BlobSection() {
    SectionTitle("Gemini blob")
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        GeminiBlob(modifier = Modifier.size(144.dp))
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleMedium)
}

@Preview(showBackground = true)
@Composable
private fun ComponentGalleryPreview() {
    OpenClawZenithTheme(darkTheme = true) {
        ComponentGalleryScreen(contentPadding = PaddingValues(0.dp))
    }
}
