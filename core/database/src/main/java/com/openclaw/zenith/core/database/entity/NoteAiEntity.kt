package com.openclaw.zenith.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * AI-extracted entity within a note (person / date / task / place). Lets the
 * UI surface action items, dates, and people quickly without re-scanning the
 * body markdown.
 */
@Entity(
    tableName = "note_entity",
    indices = [Index(value = ["kind"]), Index(value = ["note_id"])],
    foreignKeys = [
        ForeignKey(
            entity = NoteEntity::class,
            parentColumns = ["id"],
            childColumns = ["note_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class NoteAiEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "note_id") val noteId: String,
    @ColumnInfo(name = "kind") val kind: String,
    @ColumnInfo(name = "value") val value: String,
    @ColumnInfo(name = "span_start") val spanStart: Int? = null,
    @ColumnInfo(name = "span_end") val spanEnd: Int? = null,
    @ColumnInfo(name = "confidence") val confidence: Float = 1.0f,
)
