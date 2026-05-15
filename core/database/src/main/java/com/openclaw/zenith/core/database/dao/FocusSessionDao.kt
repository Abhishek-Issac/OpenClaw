package com.openclaw.zenith.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclaw.zenith.core.database.entity.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_session ORDER BY started_at DESC")
    fun observeAll(): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_session WHERE ended_at IS NULL LIMIT 1")
    fun observeActive(): Flow<FocusSessionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(session: FocusSessionEntity)

    @Query("UPDATE focus_session SET ended_at = :at WHERE id = :id")
    suspend fun endSession(
        id: String,
        at: Long,
    )
}
