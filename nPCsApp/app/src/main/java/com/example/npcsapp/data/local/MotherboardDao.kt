package com.example.npcsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.npcsapp.data.local.entities.MotherboardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MotherboardDao {
    @Query("SELECT * FROM motherboard")
    fun getMotherboards(): Flow<List<MotherboardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfMotherboards(motherboards: List<MotherboardEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMotherboard(motherboard: MotherboardEntity)

    @Query("SELECT * FROM motherboard WHERE id = :id")
    suspend fun getmMotherboardById(id: Int): MotherboardEntity?
}