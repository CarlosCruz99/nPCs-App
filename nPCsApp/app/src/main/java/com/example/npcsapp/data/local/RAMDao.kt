package com.example.npcsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.npcsapp.data.local.entities.RAMEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RAMDao {
    @Query("SELECT * FROM ram")
    fun getRAMs(): Flow<List<RAMEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfRAMs(rams: List<RAMEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRAM(ram: RAMEntity)

    @Query("SELECT * FROM ram WHERE id = :id")
    suspend fun getRAMById(id: Int): RAMEntity?
}