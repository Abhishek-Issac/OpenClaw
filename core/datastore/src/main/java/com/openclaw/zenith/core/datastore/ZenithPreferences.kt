package com.openclaw.zenith.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "zenith_prefs")

/**
 * Non-sensitive user preferences. Things like the active LLM model id and
 * theme override live here; secrets and personal content do **not**.
 *
 * Personal content lives in the SQLCipher-encrypted Room database
 * (`:core:database`) gated by [com.openclaw.zenith.core.security.BiometricGate].
 */
class ZenithPreferences(
    private val context: Context,
) {
    private val store: DataStore<Preferences> get() = context.dataStore

    val activeModelId: Flow<String?> =
        store.data.map { prefs -> prefs[ACTIVE_MODEL_ID] }

    val themeMode: Flow<ThemeMode> =
        store.data.map { prefs ->
            when (prefs[THEME_MODE]) {
                ThemeMode.LIGHT.name -> ThemeMode.LIGHT
                ThemeMode.DARK.name -> ThemeMode.DARK
                else -> ThemeMode.SYSTEM
            }
        }

    val biometricLockTimeoutMs: Flow<Long> =
        store.data.map { prefs ->
            prefs[stringPreferencesKey("biometric_lock_timeout_ms")]?.toLongOrNull()
                ?: DEFAULT_LOCK_TIMEOUT_MS
        }

    suspend fun setActiveModelId(id: String) {
        store.edit { it[ACTIVE_MODEL_ID] = id }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        store.edit { it[THEME_MODE] = mode.name }
    }

    suspend fun setBiometricLockTimeoutMs(ms: Long) {
        store.edit {
            it[stringPreferencesKey("biometric_lock_timeout_ms")] = ms.toString()
        }
    }

    enum class ThemeMode { SYSTEM, LIGHT, DARK }

    companion object {
        private val ACTIVE_MODEL_ID = stringPreferencesKey("active_model_id")
        private val THEME_MODE = stringPreferencesKey("theme_mode")
        const val DEFAULT_LOCK_TIMEOUT_MS: Long = 5L * 60L * 1000L
    }
}
