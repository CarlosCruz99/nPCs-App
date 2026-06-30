package com.example.npcsapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.npcsapp.data.local.entities.BuildEntity
import com.example.npcsapp.data.local.entities.BuildWithComponents
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildDao {
    @Query("SELECT * FROM builds")
    fun getAllBuilds(): Flow<List<BuildEntity>>

    @Transaction
    @Query("SELECT * FROM builds WHERE buildId = :buildId")
    fun searchBuildWithComponentById(buildId: Long): Flow<BuildWithComponents?>

    @Query("SELECT * FROM builds WHERE buildId = :buildId")
    fun getBuildById(buildId: Long): Flow<BuildEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuild(build: BuildEntity): Long

    @Delete
    suspend fun deleteBuild(build: BuildEntity)
}
