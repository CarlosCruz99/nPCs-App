package com.example.npcsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.npcsapp.data.local.entities.CaseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CaseDao {
    @Query("SELECT * FROM `case`")
    fun getCases(): Flow<List<CaseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfCases(cases: List<CaseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCase(pcCase: CaseEntity)

    @Query("SELECT * FROM `case` WHERE id = :id")
    suspend fun getCaseById(id: Int): CaseEntity?
}