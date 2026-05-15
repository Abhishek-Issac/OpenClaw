package com.openclaw.zenith.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openclaw.zenith.core.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query(
        """SELECT * FROM task
           WHERE status != 'DONE' AND status != 'DROPPED'
           ORDER BY priority ASC, due_at IS NULL, due_at ASC""",
    )
    fun observeOpen(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE id = :id LIMIT 1")
    suspend fun get(id: String): TaskEntity?

    @Query("SELECT * FROM task WHERE parent_id = :parentId")
    fun observeSubtasks(parentId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE source_note_id = :noteId")
    fun observeForNote(noteId: String): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Query("UPDATE task SET status = :status, completed_at = :completedAt, updated_at = :updatedAt WHERE id = :id")
    suspend fun setStatus(
        id: String,
        status: String,
        completedAt: Long?,
        updatedAt: Long,
    )
}
