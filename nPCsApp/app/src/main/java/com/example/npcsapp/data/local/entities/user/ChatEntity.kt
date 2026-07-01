package com.example.npcsapp.data.local.entities.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val chatId: String,
    val name: String? = null,
    val isGroup: Boolean = false,
    val lastMessage: String? = null,
    val lastMessageTimestamp: Long? = null
)
