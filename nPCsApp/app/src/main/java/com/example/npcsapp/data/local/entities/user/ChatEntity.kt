package com.example.npcsapp.data.local.entities.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val chatId: String = "",
    val name: String? = null, // Nombre por defecto (puede ser el del grupo)
    val isGroup: Boolean = false,
    val lastMessage: String? = null,
    val lastMessageTimestamp: Long? = null,
    val participants: List<String> = emptyList(),
    val participantNames: Map<String, String> = emptyMap() // ID -> Nombre para identificar al otro usuario
)
