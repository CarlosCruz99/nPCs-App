package com.example.npcsapp.data.repository

import android.util.Log
import com.example.npcsapp.data.local.ChatDao
import com.example.npcsapp.data.local.UserDao
import com.example.npcsapp.data.local.entities.user.ChatEntity
import com.example.npcsapp.data.local.entities.user.MessageEntity
import com.example.npcsapp.data.local.entities.user.UserEntity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ChatRepository(
    private val chatDao: ChatDao,
    private val userDao: UserDao
) {
    private val firestore = FirebaseFirestore.getInstance()
    private val chatsCollection = firestore.collection("chats")

    /**
     * Obtiene los chats en los que participa el usuario en tiempo real desde Firestore.
     */
    fun getChatsForUser(userId: String): Flow<List<ChatEntity>> = callbackFlow {
        if (userId.isBlank()) {
            trySend(emptyList())
            return@callbackFlow
        }

        val subscription = chatsCollection
            .whereArrayContains("participants", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ChatRepository", "Error en getChatsForUser: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val chats = snapshot.documents.mapNotNull { doc ->
                        try {
                            val chat = doc.toObject(ChatEntity::class.java)
                            // Mapeo manual del Map para asegurar compatibilidad total y evitar campos nulos
                            val rawNames = doc.get("participantNames") as? Map<*, *>
                            val participantNames = rawNames?.map { it.key.toString() to it.value.toString() }?.toMap() ?: emptyMap()
                            
                            chat?.copy(
                                chatId = doc.id,
                                participantNames = participantNames
                            )
                        } catch (e: Exception) {
                            Log.e("ChatRepository", "Error mapeando chat ${doc.id}: ${e.message}")
                            null
                        }
                    }.sortedByDescending { it.lastMessageTimestamp ?: 0L }
                    
                    trySend(chats)
                }
            }
        awaitClose { subscription.remove() }
    }

    /**
     * Busca o crea un chat entre dos usuarios y actualiza sus metadatos.
     */
    suspend fun getOrCreateChat(myUserId: String, myUserName: String, otherUserId: String, otherUserName: String): String {
        try {
            // Buscar si ya existe una conversación entre estos dos IDs
            val querySnapshot = chatsCollection
                .whereArrayContains("participants", myUserId)
                .get()
                .await()

            val existingChatDoc = querySnapshot.documents.find { doc ->
                val participants = doc.get("participants") as? List<*>
                participants?.contains(otherUserId) == true
            }

            if (existingChatDoc != null) {
                val chatId = existingChatDoc.id
                // Actualizamos siempre para asegurar que los nombres estén presentes y correctos
                // Eliminamos el campo 'name' si existe para chats privados, para que la UI use participantNames
                val updates = hashMapOf<String, Any>(
                    "lastMessageTimestamp" to System.currentTimeMillis(),
                    "participantNames.$myUserId" to myUserName,
                    "participantNames.$otherUserId" to otherUserName,
                    "name" to FieldValue.delete()
                )
                chatsCollection.document(chatId).update(updates).await()
                return chatId
            }

            // Si no existe, creamos uno nuevo
            val newChatId = UUID.randomUUID().toString()
            val chatData = hashMapOf(
                "chatId" to newChatId,
                "participants" to listOf(myUserId, otherUserId),
                "participantNames" to mapOf(myUserId to myUserName, otherUserId to otherUserName),
                "lastMessage" to "Inicia una conversación",
                "lastMessageTimestamp" to System.currentTimeMillis(),
                "isGroup" to false
            )
            
            chatsCollection.document(newChatId).set(chatData).await()
            return newChatId
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error en getOrCreateChat: ${e.message}")
            return ""
        }
    }

    fun getMessages(chatId: String): Flow<List<MessageEntity>> = callbackFlow {
        if (chatId.isBlank()) {
            trySend(emptyList())
            return@callbackFlow
        }
        val subscription = chatsCollection.document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(MessageEntity::class.java)?.copy(messageId = doc.id)
                    }
                    trySend(messages)
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun sendMessage(chatId: String, senderId: String, content: String) {
        if (chatId.isBlank()) return
        try {
            val timestamp = System.currentTimeMillis()
            val messageId = UUID.randomUUID().toString()
            val messageData = hashMapOf(
                "messageId" to messageId,
                "chatId" to chatId,
                "senderId" to senderId,
                "content" to content,
                "timestamp" to timestamp
            )
            chatsCollection.document(chatId).collection("messages").document(messageId).set(messageData).await()
            
            // Actualizar el timestamp y el último mensaje, disparando la lista de chats
            chatsCollection.document(chatId).update(
                "lastMessage", content, 
                "lastMessageTimestamp", timestamp
            ).await()
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error enviando mensaje: ${e.message}")
        }
    }

    suspend fun getUser(userId: String): UserEntity? = userDao.getUserById(userId)
}
