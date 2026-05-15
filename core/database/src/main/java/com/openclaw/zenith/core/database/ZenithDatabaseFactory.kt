package com.openclaw.zenith.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.openclaw.zenith.core.security.BiometricGate
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

/**
 * Builds the encrypted [ZenithDatabase] using the passphrase released by the
 * [BiometricGate] after a successful biometric authentication.
 *
 * The factory is intentionally **not** a [javax.inject.Singleton] provider —
 * it must be invoked AFTER unlock. Callers should grab the database from the
 * `EncryptedDatabaseHolder` (lives in `:app`, not here, since it depends on
 * a process-scoped state machine).
 */
object ZenithDatabaseFactory {
    @Volatile
    private var nativeLibsLoaded: Boolean = false

    private fun ensureNativeLibs(context: Context) {
        if (nativeLibsLoaded) return
        synchronized(this) {
            if (nativeLibsLoaded) return
            SQLiteDatabase.loadLibs(context.applicationContext)
            nativeLibsLoaded = true
        }
    }

    fun open(
        context: Context,
        passphrase: CharArray,
    ): ZenithDatabase {
        ensureNativeLibs(context)
        val passphraseBytes = SQLiteDatabase.getBytes(passphrase)
        val factory = SupportFactory(passphraseBytes, null, true)
        return Room
            .databaseBuilder(context, ZenithDatabase::class.java, ZenithDatabase.DATABASE_NAME)
            .openHelperFactory(factory)
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
            .also { warm(it) }
    }

    /** Issues a no-op write to surface decryption errors immediately. */
    private fun warm(db: RoomDatabase) {
        db.openHelper.writableDatabase
    }
}
