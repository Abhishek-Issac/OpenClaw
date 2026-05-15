package com.openclaw.zenith

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.openclaw.zenith.core.designsystem.theme.OpenClawZenithTheme
import com.openclaw.zenith.gallery.ComponentGalleryScreen
import com.openclaw.zenith.unlock.LockScreen
import com.openclaw.zenith.unlock.LockState
import com.openclaw.zenith.unlock.UnlockViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val unlockViewModel: UnlockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenClawZenithTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val state by unlockViewModel.state.collectAsState()
                    when (state) {
                        is LockState.Locked, is LockState.Authenticating, is LockState.Error ->
                            LockScreen(
                                state = state,
                                contentPadding = innerPadding,
                                onUnlockClick = { unlockViewModel.unlock(this@MainActivity) },
                            )
                        LockState.Unlocked ->
                            ComponentGalleryScreen(contentPadding = innerPadding)
                    }
                }
            }
        }
    }
}
