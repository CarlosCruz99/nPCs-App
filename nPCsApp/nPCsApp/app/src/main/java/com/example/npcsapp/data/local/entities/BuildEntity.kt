package com.example.npcsapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "builds")
data class BuildEntity(
    @PrimaryKey(autoGenerate = true)
    val buildId: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)