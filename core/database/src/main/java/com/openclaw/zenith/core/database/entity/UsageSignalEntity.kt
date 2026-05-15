package com.openclaw.zenith.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usage_signal",
    indices = [Index(value = ["window_start"])],
)
data class UsageSignalEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "window_start") val windowStart: Long,
    @ColumnInfo(name = "window_end") val windowEnd: Long,
    @ColumnInfo(name = "unlocks") val unlocks: Int,
    @ColumnInfo(name = "app_switches") val appSwitches: Int,
    @ColumnInfo(name = "notif_count") val notifCount: Int,
    @ColumnInfo(name = "foreground_pkg_top") val foregroundPkgTop: String? = null,
    @ColumnInfo(name = "load_score") val loadScore: Float,
)
