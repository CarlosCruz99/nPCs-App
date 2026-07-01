package com.example.npcsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.npcsapp.data.local.entities.user.ChatEntity
import com.example.npcsapp.data.local.entities.user.MessageEntity
import com.example.npcsapp.data.local.entities.user.ChatParticipantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipant(participant: ChatParticipantEntity)

    @Query("SELECT * FROM chats ORDER BY lastMessageTimestamp DESC")
    fun getAllChats(): Flow<List<ChatEntity>>

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesForChat(chatId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM chat_participants WHERE chatId = :chatId")
    suspend fun getParticipantsForChat(chatId: String): List<ChatParticipantEntity>

    @Query("""
        SELECT chatId FROM chat_participants 
        WHERE userId IN (:user1, :user2) 
        GROUP BY chatId 
        HAVING COUNT(DISTINCT userId) = 2
    """)
    suspend fun findExistingChatBetween(user1: String, user2: String): String?

    @Query("UPDATE chats SET lastMessage = :content, lastMessageTimestamp = :timestamp WHERE chatId = :chatId")
    suspend fun updateLastMessage(chatId: String, content: String, timestamp: Long)
}
