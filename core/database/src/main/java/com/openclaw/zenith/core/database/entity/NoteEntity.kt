package com.openclaw.zenith.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "note",
    indices = [Index(value = ["updated_at"])],
)
data class NoteEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "body_md") val bodyMd: String = "",
    @ColumnInfo(name = "source") val source: String,
    @ColumnInfo(name = "language") val language: String? = null,
    @ColumnInfo(name = "pinned") val pinned: Boolean = false,
    @ColumnInfo(name = "color_hint") val colorHint: String? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "deleted_at") val deletedAt: Long? = null,
)
