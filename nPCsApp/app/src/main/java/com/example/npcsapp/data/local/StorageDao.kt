package com.example.npcsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.npcsapp.data.local.entities.StorageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StorageDao {
    @Query("SELECT * FROM storage")
    fun getStorages(): Flow<List<StorageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfStorages(storages: List<StorageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStorage(storage: StorageEntity)

    @Query("SELECT * FROM storage WHERE id = :id")
    suspend fun getStorageById(id: Int): StorageEntity?
}