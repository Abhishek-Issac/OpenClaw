package com.openclaw.zenith.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openclaw.zenith.core.database.entity.NoteAiEntity
import com.openclaw.zenith.core.database.entity.NoteEntity
import com.openclaw.zenith.core.database.entity.NoteTagCrossRef
import com.openclaw.zenith.core.database.entity.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
@Suppress("TooManyFunctions")
interface NoteDao {
    @Query("SELECT * FROM note WHERE deleted_at IS NULL ORDER BY pinned DESC, updated_at DESC")
    fun observeAll(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note WHERE id = :id LIMIT 1")
    suspend fun get(id: String): NoteEntity?

    @Query("SELECT * FROM note WHERE id = :id LIMIT 1")
    fun observe(id: String): Flow<NoteEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(note: NoteEntity)

    @Update
    suspend fun update(note: NoteEntity)

    @Query("UPDATE note SET deleted_at = :at, updated_at = :at WHERE id = :id")
    suspend fun softDelete(
        id: String,
        at: Long,
    )

    @Delete
    suspend fun hardDelete(note: NoteEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: TagEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun linkTag(crossRef: NoteTagCrossRef)

    @Query(
        """SELECT t.* FROM tag t
           INNER JOIN note_tag nt ON nt.tag_id = t.id
           WHERE nt.note_id = :noteId""",
    )
    fun observeTags(noteId: String): Flow<List<TagEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntity(entity: NoteAiEntity)

    @Query("SELECT * FROM note_entity WHERE note_id = :noteId")
    fun observeEntities(noteId: String): Flow<List<NoteAiEntity>>
}
