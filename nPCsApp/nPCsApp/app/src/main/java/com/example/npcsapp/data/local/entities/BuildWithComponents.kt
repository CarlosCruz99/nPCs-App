package com.example.npcsapp.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class BuildWithComponents (
    @Embedded val build: BuildEntity,
    @Relation(
        parentColumn = "buildId",
        entityColumn = "buildId"
    )
    val components: List<BuildComponentEntity>
)