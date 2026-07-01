package com.example.npcsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.npcsapp.data.local.entities.MarketItemEntity
import com.example.npcsapp.ui.components.PrimaryButton
import com.example.npcsapp.ui.components.toPrecio
import com.example.npcsapp.ui.theme.*
import com.example.npcsapp.viewmodel.MarketViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketDetailScreen(
    itemId: Long,
    marketViewModel: MarketViewModel,
    onBack: () -> Unit,
    onStartChat: (String, String) -> Unit = { _, _ -> }
) {
    val marketItems by marketViewModel.marketItems.collectAsState()
    val item = remember(itemId, marketItems) { marketItems.find { it.id == itemId } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto", color = NeonBlue, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = NeonBlue)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Share */ }) {
                        Icon(Icons.Default.Share, "Compartir", tint = NeonBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDeep)
            )
        },
        containerColor = SurfaceDeep
    ) { padding ->
        item?.let { marketItem ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Imagen del producto
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.Black.copy(alpha = 0.2f))
                ) {
                    if (marketItem.imageUrl != null) {
                        AsyncImage(
                            model = marketItem.imageUrl,
                            contentDescription = marketItem.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Image, "Sin imagen", tint = OnSurfaceVariant.copy(alpha = 0.2f), modifier = Modifier.size(80.dp))
                        }
                    }

                    // Badge de condición
                    val conditionColor = when(marketItem.condition.uppercase()) {
                        "NUEVO" -> StatusNew
                        "OFERTA" -> StatusOffer
                        else -> StatusUsed
                    }
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                            .background(conditionColor.copy(alpha = 0.20f), RoundedCornerShape(50))
                            .border(1.dp, conditionColor.copy(alpha = 0.30f), RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            marketItem.condition.uppercase(),
                            color = conditionColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    // Título y Precio
                    Text(
                        text = marketItem.title,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = marketItem.price.toPrecio(),
                        color = NeonBlue,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Ubicación
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(marketItem.location, color = OnSurfaceVariant, fontSize = 14.sp)
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 24.dp),
                        color = Color.White.copy(alpha = 0.08f)
                    )

                    // Información del Vendedor
                    Text("VENDEDOR", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(PrimaryContainer.copy(alpha = 0.2f), CircleShape)
                                .border(1.dp, NeonBlue.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, null, tint = NeonBlue, modifier = Modifier.size(24.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(marketItem.sellerName, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("Miembro desde hace poco", color = OnSurfaceVariant, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Descripción
                    Text("DESCRIPCIÓN", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = marketItem.description,
                        color = OnSurface,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // Acciones
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onStartChat(marketItem.sellerId, marketItem.sellerName) },
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                        ) {
                            Icon(Icons.Default.ChatBubbleOutline, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Mensaje", fontWeight = FontWeight.Bold)
                        }

                        PrimaryButton(
                            text = "Comprar ahora",
                            onClick = { /* TODO: Checkout */ },
                            modifier = Modifier.weight(1f).height(56.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        } ?: run {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonBlue)
            }
        }
    }
}
