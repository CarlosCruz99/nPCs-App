package com.example.npcsapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "storage")
data class StorageEntity (
    @PrimaryKey
    val id: Int,
    val name: String,
    val price: Float,
    val capacity: Int?,
    @SerializedName("price_per_gb")
    val pricePerGB: Float?,
    val type: String?,
    val cache: Int?,
    @SerializedName("form_factor")
    val formFactor: String?,
    val `interface`: String?,
    val image: String?,
    @SerializedName("amazon_link")
    val amazonLink: String?
)