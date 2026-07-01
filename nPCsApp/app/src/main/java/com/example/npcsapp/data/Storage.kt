package com.example.npcsapp.data

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Storage (
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