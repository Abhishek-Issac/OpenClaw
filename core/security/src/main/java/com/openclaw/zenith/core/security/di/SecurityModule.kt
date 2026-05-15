package com.openclaw.zenith.core.security.di

import android.content.Context
import com.openclaw.zenith.core.security.BiometricGate
import com.openclaw.zenith.core.security.KeyStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {
    @Provides
    @Singleton
    fun provideKeyStoreManager(
        @ApplicationContext context: Context,
    ): KeyStoreManager = KeyStoreManager(context)

    @Provides
    @Singleton
    fun provideBiometricGate(keyStoreManager: KeyStoreManager): BiometricGate = BiometricGate(keyStoreManager)
}
