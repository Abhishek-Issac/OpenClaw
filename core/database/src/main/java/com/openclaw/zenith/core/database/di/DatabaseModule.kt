package com.openclaw.zenith.core.database.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Placeholder Hilt module — [com.openclaw.zenith.core.database.EncryptedDatabaseHolder]
 * is already annotated with `@Singleton @Inject`, so Hilt can construct it
 * directly. Concrete DAO providers live behind the holder's reactive flow.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule
