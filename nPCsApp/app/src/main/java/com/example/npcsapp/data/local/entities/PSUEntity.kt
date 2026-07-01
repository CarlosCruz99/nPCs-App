package com.example.npcsapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "psu")
data class PSUEntity (
    @PrimaryKey
    val id: Int,
    val name: String,
    val price: Float,
    val type: String?,
    val efficiency: String?,
    val wattage: Int?,
    val modular: String?,
    val color: String?,
    val image: String?,
    @SerializedName("amazon_link")
    val amazonLink: String?
)