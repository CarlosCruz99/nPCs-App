package com.example.npcsapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "gpu")
data class GPUEntity (
    @PrimaryKey
    val id: Int,
    val name: String,
    val price: Float,
    val chipset: String,
    val memory: Float,
    @SerializedName("core_clock")
    val coreClock: Int,

    @SerializedName("core_boost")
    val coreBoost: Int,
    val color: String,
    val length: Int,
    val image: String,

    @SerializedName("amazon_link")
    val amazonLink: String?
)