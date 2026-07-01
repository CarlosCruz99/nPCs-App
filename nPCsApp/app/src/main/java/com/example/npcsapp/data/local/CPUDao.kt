package com.example.npcsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.npcsapp.data.local.entities.CPUEntity
import com.example.npcsapp.data.local.entities.GPUEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CPUDao {
    @Query("SELECT * FROM cpu")
    fun getCPUs(): Flow<List<CPUEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfCPUs(cpus: List<CPUEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCPU(cpu: CPUEntity)

    @Query("SELECT * FROM cpu WHERE id = :id")
    suspend fun getCPUById(id: Int): CPUEntity?
}