package com.example.npcsapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "market_items")
data class MarketItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val price: Float = 0f,
    val description: String = "",
    val location: String = "",
    val sellerName: String = "",
    val sellerId: String = "",
    val condition: String = "", // "NUEVO", "USADO", "OFERTA"
    val imageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
