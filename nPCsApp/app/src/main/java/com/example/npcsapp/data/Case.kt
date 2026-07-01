package com.example.npcsapp.data

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Case (
    val id: Int,
    val name: String,
    val price: Float,
    val type: String?,
    val color: String?,
    val psu: Int?,
    @SerializedName("side_panel")
    val sidePanel: String?,
    @SerializedName("external_volume")
    val externalVolume: Float?,
    @SerializedName("internal_35_bays")
    val interna35Bays: Int?,
    val image: String?,
    @SerializedName("amazon_link")
    val amazonLink: String?
)