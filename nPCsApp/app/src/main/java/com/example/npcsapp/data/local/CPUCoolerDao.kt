package com.example.npcsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.npcsapp.data.local.entities.CPUCoolerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CPUCoolerDao {
    @Query("SELECT * FROM cpu_cooler")
    fun getCPUCoolers(): Flow<List<CPUCoolerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfCPUCoolers(cpuCoolers: List<CPUCoolerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCPUCooler(cpuCooler: CPUCoolerEntity)

    @Query("SELECT * FROM cpu_cooler WHERE id = :id")
    suspend fun getCPUCoolerById(id: Int): CPUCoolerEntity?
}