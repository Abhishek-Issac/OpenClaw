package com.openclaw.zenith.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reminder",
    indices = [
        Index(value = ["trigger_at"]),
        Index(value = ["task_id"]),
        Index(value = ["note_id"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = NoteEntity::class,
            parentColumns = ["id"],
            childColumns = ["note_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class ReminderEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "task_id") val taskId: String? = null,
    @ColumnInfo(name = "note_id") val noteId: String? = null,
    @ColumnInfo(name = "trigger_kind") val triggerKind: String,
    @ColumnInfo(name = "trigger_at") val triggerAt: Long? = null,
    @ColumnInfo(name = "rrule") val rrule: String? = null,
    @ColumnInfo(name = "geofence_lat") val geofenceLat: Double? = null,
    @ColumnInfo(name = "geofence_lng") val geofenceLng: Double? = null,
    @ColumnInfo(name = "geofence_radius_m") val geofenceRadiusM: Double? = null,
    @ColumnInfo(name = "context_tag") val contextTag: String? = null,
    @ColumnInfo(name = "urgency") val urgency: Int = 2,
    @ColumnInfo(name = "fired_at") val firedAt: Long? = null,
    @ColumnInfo(name = "dismissed_at") val dismissedAt: Long? = null,
    @ColumnInfo(name = "work_request_id") val workRequestId: String? = null,
)
