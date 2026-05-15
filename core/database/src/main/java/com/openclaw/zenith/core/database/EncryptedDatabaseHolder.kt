package com.openclaw.zenith.core.database

import android.content.Context
import com.openclaw.zenith.core.common.coroutine.ApplicationScope
import com.openclaw.zenith.core.common.coroutine.IoDispatcher
import com.openclaw.zenith.core.common.log.Log
import com.openclaw.zenith.core.security.BiometricGate
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Lifecycle-aware wrapper that keeps the [ZenithDatabase] open while the gate
 * is unlocked and closes it on lock. Feature modules consume this Flow rather
 * than the [ZenithDatabase] directly so the gate state propagates naturally
 * through Compose.
 *
 * - State.Locked → database emits null, feature flows show "biometric required"
 * - State.Unlocked → database is materialised on the IO dispatcher
 *
 * On lock, the previous instance is closed asynchronously to release the
 * SQLCipher handle.
 */
@Singleton
class EncryptedDatabaseHolder
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val biometricGate: BiometricGate,
        @IoDispatcher private val io: CoroutineDispatcher,
        @ApplicationScope private val scope: CoroutineScope,
    ) {
        private val _database = MutableStateFlow<ZenithDatabase?>(null)
        val database: StateFlow<ZenithDatabase?> = _database

        init {
            scope.launch {
                biometricGate.state
                    .map { (it as? BiometricGate.State.Unlocked)?.passphrase }
                    .distinctUntilChanged { a, b -> a contentEqualsNullable b }
                    .onEach { passphrase ->
                        if (passphrase == null) {
                            close()
                        } else {
                            open(passphrase)
                        }
                    }.stateIn(scope, SharingStarted.Eagerly, null)
            }
        }

        private suspend fun open(passphrase: CharArray) =
            withContext(io) {
                runCatching { ZenithDatabaseFactory.open(context, passphrase.copyOf()) }
                    .onSuccess { db ->
                        Log.i(TAG, "Encrypted database opened")
                        _database.value = db
                    }.onFailure { e ->
                        Log.e(TAG, "Failed to open encrypted database", e)
                        _database.value = null
                    }
            }

        private suspend fun close() =
            withContext(io) {
                val current = _database.value
                _database.value = null
                runCatching { current?.close() }
                    .onFailure { Log.w(TAG, "Error while closing database", it) }
            }

        private companion object {
            const val TAG = "EncryptedDb"
        }
    }

private infix fun CharArray?.contentEqualsNullable(other: CharArray?): Boolean =
    when {
        this == null && other == null -> true
        this == null || other == null -> false
        else -> this contentEquals other
    }
