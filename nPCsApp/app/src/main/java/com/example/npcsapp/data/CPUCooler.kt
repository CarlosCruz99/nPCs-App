package com.example.npcsapp.data

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class CPUCooler (
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