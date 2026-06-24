package com.example.npcsapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.npcsapp.data.local.entities.BuildComponentEntity
import com.example.npcsapp.data.local.entities.BuildEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildComponentDao {
    @Query("SELECT * FROM build_components")
    fun getAllBuildComponents(): Flow<List<BuildComponentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuildComponent(buildComponent: BuildComponentEntity)

    @Delete
    suspend fun deleteBuildComponent(build: BuildComponentEntity)

    @Query("DELETE FROM build_components WHERE buildId = :buildId")
    suspend fun deleteAllComponentsForBuild(buildId: Long)

}