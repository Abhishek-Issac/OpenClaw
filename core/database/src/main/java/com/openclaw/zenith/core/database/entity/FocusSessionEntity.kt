package com.openclaw.zenith.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_session")
data class FocusSessionEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "started_at") val startedAt: Long,
    @ColumnInfo(name = "ended_at") val endedAt: Long? = null,
    @ColumnInfo(name = "mode") val mode: String,
    @ColumnInfo(name = "trigger") val trigger: String,
    @ColumnInfo(name = "cognitive_load") val cognitiveLoad: Float? = null,
    @ColumnInfo(name = "app_blocklist_json") val appBlocklistJson: String = "[]",
    @ColumnInfo(name = "notes") val notes: String? = null,
)
