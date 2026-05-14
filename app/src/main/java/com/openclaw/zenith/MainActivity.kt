package com.openclaw.zenith

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.openclaw.zenith.ui.theme.OpenClawZenithTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenClawZenithTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LandingContent(modifier = Modifier.padding(innerPadding).fillMaxSize())
                }
            }
        }
    }
}

@Composable
private fun LandingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "OpenClaw: Zenith", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Phase 0 — scaffold online", style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
private fun LandingPreview() {
    OpenClawZenithTheme {
        LandingContent()
    }
}
