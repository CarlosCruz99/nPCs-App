package com.example.npcsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.npcsapp.data.local.entities.PSUEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PSUDao {
    @Query("SELECT * FROM psu")
    fun getPSUs(): Flow<List<PSUEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfPSUs(psus: List<PSUEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPSU(psu: PSUEntity)

    @Query("SELECT * FROM psu WHERE id = :id")
    suspend fun getPSUById(id: Int): PSUEntity?
}