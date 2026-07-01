package com.example.npcsapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cpu_cooler")
data class CPUCoolerEntity (
    @PrimaryKey
    val id: Int,
    val name: String,
    val price: Float,
    @SerializedName("min_rpm")
    val minRPM: Int?,
    @SerializedName("min_noise_level")
    val minNoiseLevel: Float?,
    val color: String?,
    val size: Int?,
    val image: String?,
    @SerializedName("amazon_link")
    val amazonLink: String?,
    @SerializedName("max_rpm")
    val maxRPM: Int?,
    @SerializedName("max_noise_level")
    val maxNoiseLevel: Float?
)