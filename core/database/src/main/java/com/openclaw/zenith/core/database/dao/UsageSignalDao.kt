package com.openclaw.zenith.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclaw.zenith.core.database.entity.UsageSignalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageSignalDao {
    @Query("SELECT * FROM usage_signal WHERE window_start >= :since ORDER BY window_start DESC")
    fun observeSince(since: Long): Flow<List<UsageSignalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(signal: UsageSignalEntity)

    @Query("DELETE FROM usage_signal WHERE window_end < :before")
    suspend fun prune(before: Long)
}
