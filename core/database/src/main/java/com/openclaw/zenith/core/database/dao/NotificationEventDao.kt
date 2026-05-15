package com.openclaw.zenith.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclaw.zenith.core.database.entity.NotificationEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationEventDao {
    @Query("SELECT * FROM notification_event ORDER BY posted_at DESC LIMIT :limit")
    fun observeRecent(limit: Int = 200): Flow<List<NotificationEventEntity>>

    @Query("SELECT * FROM notification_event WHERE pkg = :pkg ORDER BY posted_at DESC LIMIT :limit")
    fun observeForPackage(
        pkg: String,
        limit: Int = 50,
    ): Flow<List<NotificationEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(event: NotificationEventEntity)

    @Query("DELETE FROM notification_event WHERE posted_at < :before")
    suspend fun prune(before: Long)
}
