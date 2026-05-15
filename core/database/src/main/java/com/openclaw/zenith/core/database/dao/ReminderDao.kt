package com.openclaw.zenith.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclaw.zenith.core.database.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder WHERE fired_at IS NULL ORDER BY trigger_at IS NULL, trigger_at ASC")
    fun observePending(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminder WHERE task_id = :taskId")
    fun observeForTask(taskId: String): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminder WHERE id = :id LIMIT 1")
    suspend fun get(id: String): ReminderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(reminder: ReminderEntity)

    @Query("UPDATE reminder SET fired_at = :at WHERE id = :id")
    suspend fun markFired(
        id: String,
        at: Long,
    )

    @Query("UPDATE reminder SET dismissed_at = :at WHERE id = :id")
    suspend fun markDismissed(
        id: String,
        at: Long,
    )

    @Query("DELETE FROM reminder WHERE id = :id")
    suspend fun delete(id: String)
}
