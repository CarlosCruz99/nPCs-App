package com.example.npcsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.npcsapp.data.local.entities.user.ChatEntity
import com.example.npcsapp.ui.theme.*
import com.example.npcsapp.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    viewModel: ChatViewModel,
    onNavigateToChat: (String) -> Unit,
    onBack: () -> Unit
) {
    val chats by viewModel.chats.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mensajes", color = NeonBlue, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = NeonBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.88f))
            )
        },
        containerColor = SurfaceDeep
    ) { padding ->
        if (chats.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Person, null, tint = OnSurfaceVariant.copy(alpha = 0.2f), modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("No tienes conversaciones aún", color = OnSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(chats) { chat ->
                    // RESOLUCIÓN DE NOMBRE: Buscamos al participante que NO sea el usuario actual
                    val displayName = if (currentUserId != null) {
                        val otherName = chat.participantNames.filterKeys { it != currentUserId }.values.firstOrNull()
                        // Si no hay mapa de nombres, intentamos usar el nombre del chat sólo si no es el nuestro
                        otherName ?: if (chat.name != null && chat.participantNames[currentUserId] != chat.name) {
                            chat.name
                        } else {
                            "Usuario"
                        }
                    } else {
                        chat.name ?: "Cargando..."
                    }
                    
                    ChatItem(
                        name = displayName ?: "Usuario",
                        lastMessage = chat.lastMessage ?: "Inicia una conversación",
                        timestamp = chat.lastMessageTimestamp,
                        onClick = { onNavigateToChat(chat.chatId) }
                    )
                }
            }
        }
    }
}

@Composable
fun ChatItem(
    name: String,
    lastMessage: String,
    timestamp: Long?,
    onClick: () -> Unit
) {
    val time = timestamp?.let {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(it))
    } ?: ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(PrimaryContainer.copy(alpha = 0.2f))
                .border(1.dp, NeonBlue.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, null, tint = NeonBlue, modifier = Modifier.size(28.dp))
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = time,
                    color = OnSurfaceVariant.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
            
            Spacer(Modifier.height(4.dp))
            
            Text(
                text = lastMessage,
                color = OnSurfaceVariant,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
