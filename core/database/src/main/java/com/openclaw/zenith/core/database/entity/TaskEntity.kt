package com.openclaw.zenith.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "task",
    indices = [
        Index(value = ["status", "due_at"]),
        Index(value = ["parent_id"]),
        Index(value = ["source_note_id"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = NoteEntity::class,
            parentColumns = ["id"],
            childColumns = ["source_note_id"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
)
data class TaskEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "parent_id") val parentId: String? = null,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "details_md") val detailsMd: String? = null,
    @ColumnInfo(name = "status") val status: String = "TODO",
    @ColumnInfo(name = "priority") val priority: Int = 2,
    @ColumnInfo(name = "due_at") val dueAt: Long? = null,
    @ColumnInfo(name = "source_note_id") val sourceNoteId: String? = null,
    @ColumnInfo(name = "ai_extracted") val aiExtracted: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "completed_at") val completedAt: Long? = null,
)
