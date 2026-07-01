package com.example.npcsapp.data

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Motherboard (
    val id: Int,
    val name: String,
    val price: Float,
    val socket: String?,
    @SerializedName("form_factor")
    val formFactor: String?,
    @SerializedName("max_memory")
    val maxMemory: Float?,
    @SerializedName("memory_slots")
    val memorySlots: Int?,
    val color: String?,
    val image: String?,
    @SerializedName("amazon_link")
    val amazonLink: String?
)