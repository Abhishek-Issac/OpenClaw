package com.openclaw.zenith.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclaw.zenith.core.database.entity.AiModelEntity
import com.openclaw.zenith.core.database.entity.ModelSwapLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AiModelDao {
    @Query("SELECT * FROM ai_model ORDER BY provider ASC, display_name ASC")
    fun observeAll(): Flow<List<AiModelEntity>>

    @Query("SELECT * FROM ai_model WHERE id = :id LIMIT 1")
    suspend fun get(id: String): AiModelEntity?

    @Query("SELECT * FROM ai_model WHERE is_default = 1 LIMIT 1")
    suspend fun getDefault(): AiModelEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(model: AiModelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(models: List<AiModelEntity>)

    @Query("DELETE FROM ai_model WHERE provider = :provider")
    suspend fun clearProvider(provider: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun logSwap(entry: ModelSwapLogEntity)

    @Query("SELECT * FROM model_swap_log ORDER BY at DESC LIMIT :limit")
    fun observeSwapLog(limit: Int = 50): Flow<List<ModelSwapLogEntity>>
}
