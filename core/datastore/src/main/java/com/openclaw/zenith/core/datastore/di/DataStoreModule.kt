package com.openclaw.zenith.core.datastore.di

import android.content.Context
import com.openclaw.zenith.core.datastore.ZenithPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideZenithPreferences(
        @ApplicationContext context: Context,
    ): ZenithPreferences = ZenithPreferences(context)
}
