package com.openclaw.zenith.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.openclaw.zenith.core.database.dao.AiModelDao
import com.openclaw.zenith.core.database.dao.FocusSessionDao
import com.openclaw.zenith.core.database.dao.NoteDao
import com.openclaw.zenith.core.database.dao.NotificationEventDao
import com.openclaw.zenith.core.database.dao.ReminderDao
import com.openclaw.zenith.core.database.dao.TaskDao
import com.openclaw.zenith.core.database.dao.UsageSignalDao
import com.openclaw.zenith.core.database.entity.AiModelEntity
import com.openclaw.zenith.core.database.entity.FocusSessionEntity
import com.openclaw.zenith.core.database.entity.ModelSwapLogEntity
import com.openclaw.zenith.core.database.entity.NoteAiEntity
import com.openclaw.zenith.core.database.entity.NoteEntity
import com.openclaw.zenith.core.database.entity.NoteTagCrossRef
import com.openclaw.zenith.core.database.entity.NotificationEventEntity
import com.openclaw.zenith.core.database.entity.ReminderEntity
import com.openclaw.zenith.core.database.entity.TagEntity
import com.openclaw.zenith.core.database.entity.TaskEntity
import com.openclaw.zenith.core.database.entity.UsageSignalEntity

@Database(
    version = 1,
    exportSchema = true,
    entities = [
        NoteEntity::class,
        TagEntity::class,
        NoteTagCrossRef::class,
        NoteAiEntity::class,
        TaskEntity::class,
        ReminderEntity::class,
        FocusSessionEntity::class,
        UsageSignalEntity::class,
        NotificationEventEntity::class,
        AiModelEntity::class,
        ModelSwapLogEntity::class,
    ],
)
abstract class ZenithDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    abstract fun taskDao(): TaskDao

    abstract fun reminderDao(): ReminderDao

    abstract fun focusSessionDao(): FocusSessionDao

    abstract fun usageSignalDao(): UsageSignalDao

    abstract fun notificationEventDao(): NotificationEventDao

    abstract fun aiModelDao(): AiModelDao

    companion object {
        const val DATABASE_NAME: String = "zenith.db"
    }
}
