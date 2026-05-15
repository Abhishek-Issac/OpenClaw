package com.openclaw.zenith.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_model")
data class AiModelEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "provider") val provider: String,
    @ColumnInfo(name = "display_name") val displayName: String,
    @ColumnInfo(name = "modality") val modality: String,
    @ColumnInfo(name = "context_window") val contextWindow: Int? = null,
    @ColumnInfo(name = "default_temp") val defaultTemp: Float = 0.7f,
    @ColumnInfo(name = "endpoint_url") val endpointUrl: String? = null,
    @ColumnInfo(name = "capabilities_json") val capabilitiesJson: String = "[]",
    @ColumnInfo(name = "last_seen_at") val lastSeenAt: Long,
    @ColumnInfo(name = "is_default") val isDefault: Boolean = false,
)

@Entity(tableName = "model_swap_log")
data class ModelSwapLogEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "from_model") val fromModel: String? = null,
    @ColumnInfo(name = "to_model") val toModel: String,
    @ColumnInfo(name = "reason") val reason: String? = null,
    @ColumnInfo(name = "at") val at: Long,
)
