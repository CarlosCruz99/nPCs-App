package com.example.npcsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.npcsapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellScreen(
    onBack: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vender Componente", fontWeight = FontWeight.Bold, color = NeonBlue) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = NeonBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDeep)
            )
        },
        containerColor = SurfaceDeep
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Placeholder para subir foto (Glassmorphism)
            Card(
                modifier = Modifier.fillMaxWidth().height(160.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard.copy(alpha = 0.7f)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = NeonBlue, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Subir imágenes del hardware", color = OnSurfaceVariant, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // TextFields estilizados
            CustomDarkTextField(value = title, onValueChange = { title = it }, label = "Título del anuncio")
            Spacer(modifier = Modifier.height(16.dp))
            CustomDarkTextField(value = price, onValueChange = { price = it }, label = "Precio en USD")
            Spacer(modifier = Modifier.height(16.dp))
            CustomDarkTextField(value = description, onValueChange = { description = it }, label = "Descripción técnica (Estado, uso...)", singleLine = false, modifier = Modifier.height(120.dp))

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
            ) {
                Text("Publicar en el Mercado", fontWeight = FontWeight.Bold, color = Color(0xFF003640), fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(80.dp)) // Espacio Navbar
        }
    }
}

// COMPONENTE: TextField personalizado para tema oscuro
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDarkTextField(
    value: String, onValueChange: (String) -> Unit, label: String,
    singleLine: Boolean = true, modifier: Modifier = Modifier.fillMaxWidth()
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = OutlineVariant) },
        modifier = modifier,
        singleLine = singleLine,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonCyan,
            unfocusedBorderColor = OutlineVariant,
            focusedTextColor = OnSurface,
            unfocusedTextColor = OnSurface,
            focusedContainerColor = SurfaceDeep,
            unfocusedContainerColor = SurfaceDeep,
            cursorColor = NeonCyan
        )
    )
}
