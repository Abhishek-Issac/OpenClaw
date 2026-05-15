package com.openclaw.zenith.core.security

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Gates access to the encrypted database passphrase behind a [BiometricPrompt]
 * authentication. Cold start → locked. After successful auth, the gate stays
 * unlocked until [lock] is called (typically from a scheduled timeout or when
 * the app moves to background past the user's grace period).
 */
class BiometricGate(
    private val keyStoreManager: KeyStoreManager,
) {
    private val _state = MutableStateFlow<State>(State.Locked)
    val state: StateFlow<State> = _state.asStateFlow()

    fun isUnlocked(): Boolean = _state.value is State.Unlocked

    fun currentPassphrase(): CharArray? = (_state.value as? State.Unlocked)?.passphrase

    /** Drop the in-memory passphrase. Idempotent. */
    fun lock() {
        val current = _state.value
        if (current is State.Unlocked) {
            current.passphrase.fill('0')
        }
        _state.value = State.Locked
    }

    /**
     * Checks whether biometric auth is usable on the current device.
     */
    fun status(context: Context): BiometricStatus {
        val manager = BiometricManager.from(context)
        val auths =
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
        return when (manager.canAuthenticate(auths)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricStatus.AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricStatus.NO_HARDWARE
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricStatus.UNAVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricStatus.NONE_ENROLLED
            else -> BiometricStatus.UNKNOWN
        }
    }

    /**
     * Show the biometric prompt and, on success, materialise the passphrase
     * from [KeyStoreManager]. Suspends until the user cancels or confirms.
     */
    suspend fun unlock(activity: FragmentActivity): Result =
        suspendCoroutine { cont ->
            val executor = ContextCompat.getMainExecutor(activity)
            val prompt =
                BiometricPrompt(
                    activity,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            val passphrase = keyStoreManager.loadOrCreatePassphrase()
                            _state.value = State.Unlocked(passphrase)
                            cont.resume(Result.Success)
                        }

                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence,
                        ) {
                            cont.resume(Result.Error(errorCode, errString.toString()))
                        }

                        override fun onAuthenticationFailed() {
                            // User can retry; keep waiting.
                        }
                    },
                )
            val info =
                BiometricPrompt
                    .PromptInfo
                    .Builder()
                    .setTitle("Unlock OpenClaw")
                    .setSubtitle("Your notes, tasks, and reminders are encrypted at rest")
                    .setAllowedAuthenticators(
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or
                            BiometricManager.Authenticators.DEVICE_CREDENTIAL,
                    ).build()
            prompt.authenticate(info)
        }

    sealed class State {
        data object Locked : State()

        data class Unlocked(
            val passphrase: CharArray,
        ) : State() {
            override fun equals(other: Any?): Boolean = other is Unlocked && other.passphrase contentEquals passphrase

            override fun hashCode(): Int = passphrase.contentHashCode()
        }
    }

    sealed class Result {
        data object Success : Result()

        data class Error(
            val code: Int,
            val message: String,
        ) : Result()
    }

    enum class BiometricStatus { AVAILABLE, NO_HARDWARE, UNAVAILABLE, NONE_ENROLLED, UNKNOWN }
}
