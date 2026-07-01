package com.example.npcsapp.data

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class CPU (
    val id: Int,
    val name: String,
    val price: Float,
    @SerializedName("core_count")
    val coreCount: Int?,
    @SerializedName("core_clock")
    val coreClock: Float?,
    @SerializedName("boost_clock")
    val boostClock: Float?,
    val microarchitecture: String?,
    val tdp: Int?,
    val graphics: String?,
    val image: String?,
    @SerializedName("amazon_link")
    val amazonLink: String?
)