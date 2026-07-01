package com.example.npcsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.npcsapp.data.local.entities.GPUEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GPUDao {
    @Query("SELECT * FROM gpu")
    fun getGPUs(): Flow<List<GPUEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfGPUs(gpus: List<GPUEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGPU(gpu: GPUEntity)

    @Query("SELECT * FROM gpu WHERE id = :id")
    suspend fun getGPUById(id: Int): GPUEntity?

}