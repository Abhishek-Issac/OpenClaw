package com.openclaw.zenith.unlock

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclaw.zenith.core.security.BiometricGate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LockState {
    data object Locked : LockState

    data object Authenticating : LockState

    data object Unlocked : LockState

    data class Error(
        val message: String,
    ) : LockState
}

@HiltViewModel
class UnlockViewModel
    @Inject
    constructor(
        private val biometricGate: BiometricGate,
    ) : ViewModel() {
        private val _state =
            MutableStateFlow<LockState>(
                if (biometricGate.isUnlocked()) LockState.Unlocked else LockState.Locked,
            )
        val state: StateFlow<LockState> = _state.asStateFlow()

        fun unlock(activity: FragmentActivity) {
            if (_state.value is LockState.Authenticating) return
            _state.value = LockState.Authenticating
            viewModelScope.launch {
                when (val result = biometricGate.unlock(activity)) {
                    is BiometricGate.Result.Success -> _state.value = LockState.Unlocked
                    is BiometricGate.Result.Error -> _state.value = LockState.Error(result.message)
                }
            }
        }

        fun lock() {
            biometricGate.lock()
            _state.value = LockState.Locked
        }
    }
