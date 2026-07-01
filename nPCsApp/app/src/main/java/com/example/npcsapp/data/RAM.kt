package com.example.npcsapp.data

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class RAM (
    val id: Int,
    val name: String,
    val price: Float,
    val generation: Int?,
    val speed: Int?,
    val modules: Int?,
    @SerializedName("module_memory")
    val moduleMemory: Int?,
    @SerializedName("price_per_gb")
    val pricePerGB: Float?,
    val color: String?,
    @SerializedName("first_word_latency")
    val firstWordLatency: Float?,
    @SerializedName("cas_latency")
    val casLatency: Float?,
    val image: String?,
    @SerializedName("amazon_link")
    val amazonLink: String?
)