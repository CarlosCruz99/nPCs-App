package com.example.npcsapp.data

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class PSU (
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