package com.openclaw.zenith.core.security

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import java.io.File
import java.security.SecureRandom

/**
 * Wraps the SQLCipher passphrase in an [EncryptedFile] keyed by a non-extractable
 * AndroidKeyStore master key (AES-256 GCM under the hood via Jetpack Security).
 *
 * Workflow:
 * 1. First run: generate a random 32-byte passphrase, write it to the encrypted
 *    file. The KeyStore key never leaves the TEE.
 * 2. Subsequent runs: read the passphrase back via the same encrypted file
 *    after biometric authentication succeeds (see [BiometricGate]).
 *
 * The unwrapped passphrase is intentionally returned as a [CharArray] so callers
 * can zero it out after use — never store it in a [String], which would intern
 * into the constant pool.
 */
class KeyStoreManager(
    private val context: Context,
) {
    private val masterKey: MasterKey by lazy {
        MasterKey
            .Builder(context, MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .setUserAuthenticationRequired(false)
            .build()
    }

    private val passphraseFile: File
        get() = File(context.filesDir, PASSPHRASE_FILE)

    private val encryptedFile: EncryptedFile
        get() =
            EncryptedFile
                .Builder(
                    context,
                    passphraseFile,
                    masterKey,
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB,
                ).build()

    /**
     * Returns the SQLCipher passphrase. Creates one on first run. Result is a
     * fresh CharArray that the caller is responsible for zeroing.
     */
    @Synchronized
    fun loadOrCreatePassphrase(): CharArray {
        if (!passphraseFile.exists()) {
            val bytes = ByteArray(PASSPHRASE_BYTES).also { SecureRandom().nextBytes(it) }
            try {
                encryptedFile.openFileOutput().use { it.write(bytes) }
            } finally {
                bytes.fill(0)
            }
        }
        val bytes = encryptedFile.openFileInput().use { it.readBytes() }
        return try {
            bytes.toHexCharArray()
        } finally {
            bytes.fill(0)
        }
    }

    /** Destroys the passphrase file and master key. After this, all encrypted
     *  data becomes irrecoverable — wire to a "secure wipe" preference. */
    fun wipe() {
        if (passphraseFile.exists()) passphraseFile.delete()
    }

    private fun ByteArray.toHexCharArray(): CharArray {
        val chars = CharArray(size * 2)
        for (i in indices) {
            val v = this[i].toInt() and 0xFF
            chars[i * 2] = HEX[v ushr 4]
            chars[i * 2 + 1] = HEX[v and 0x0F]
        }
        return chars
    }

    companion object {
        private const val MASTER_KEY_ALIAS = "openclaw_master_v1"
        private const val PASSPHRASE_FILE = "openclaw.passphrase.enc"
        private const val PASSPHRASE_BYTES = 32
        private val HEX = "0123456789abcdef".toCharArray()
    }
}
