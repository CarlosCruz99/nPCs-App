package com.example.npcsapp.data.local.entities.user

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "chat_participants",
    primaryKeys = ["chatId", "userId"],
    indices = [Index("chatId"), Index("userId")]
)
data class ChatParticipantEntity(
    val chatId: String,
    val userId: String
)
