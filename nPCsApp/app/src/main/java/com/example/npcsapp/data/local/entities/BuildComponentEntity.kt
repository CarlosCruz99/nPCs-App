package com.example.npcsapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "build_components",
)
data class BuildComponentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val buildId: Long,
    val componentType: String,
    val componentId: Int
)