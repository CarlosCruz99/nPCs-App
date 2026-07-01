package com.example.npcsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.npcsapp.data.local.entities.MarketItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketItemDao {
    @Query("SELECT * FROM market_items ORDER BY createdAt DESC")
    fun getAllMarketItems(): Flow<List<MarketItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarketItem(item: MarketItemEntity)

    @Query("SELECT * FROM market_items WHERE id = :id")
    suspend fun getMarketItemById(id: Long): MarketItemEntity?

    @Query("DELETE FROM market_items WHERE id = :id")
    suspend fun deleteMarketItem(id: Long)
}
