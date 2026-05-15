package com.openclaw.zenith.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notification_event",
    indices = [
        Index(value = ["posted_at"]),
        Index(value = ["pkg"]),
        Index(value = ["focus_session_id"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = FocusSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["focus_session_id"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
)
data class NotificationEventEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "posted_at") val postedAt: Long,
    @ColumnInfo(name = "pkg") val pkg: String,
    @ColumnInfo(name = "channel_id") val channelId: String? = null,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "text") val text: String? = null,
    @ColumnInfo(name = "category") val category: String? = null,
    @ColumnInfo(name = "importance_in") val importanceIn: Int,
    @ColumnInfo(name = "importance_out") val importanceOut: Int,
    @ColumnInfo(name = "action") val action: String,
    @ColumnInfo(name = "summary") val summary: String? = null,
    @ColumnInfo(name = "reply_suggestions_json") val replySuggestionsJson: String? = null,
    @ColumnInfo(name = "focus_session_id") val focusSessionId: String? = null,
    @ColumnInfo(name = "model_id") val modelId: String,
)
