package com.example.npcsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.npcsapp.data.local.entities.user.ChatEntity
import com.example.npcsapp.data.local.entities.user.MessageEntity
import com.example.npcsapp.data.repository.ChatRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val chats: StateFlow<List<ChatEntity>> = _currentUserId
        .flatMapLatest { userId ->
            if (userId != null) {
                repository.getChatsForUser(userId)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _currentChatId = MutableStateFlow<String?>(null)
    
    @OptIn(ExperimentalCoroutinesApi::class)
    val messages: StateFlow<List<MessageEntity>> = _currentChatId
        .flatMapLatest { chatId ->
            if (chatId != null) repository.getMessages(chatId)
            else flowOf(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun initViewModel(userId: String) {
        _currentUserId.value = userId
    }

    fun selectChat(chatId: String) {
        _currentChatId.value = chatId
    }

    suspend fun startChatWith(myUserId: String, myUserName: String, otherUserId: String, otherUserName: String): String {
        val chatId = repository.getOrCreateChat(myUserId, myUserName, otherUserId, otherUserName)
        selectChat(chatId)
        return chatId
    }

    fun sendMessage(chatId: String, senderId: String, content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            repository.sendMessage(chatId, senderId, content)
        }
    }
}

class ChatViewModelFactory(private val repository: ChatRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
