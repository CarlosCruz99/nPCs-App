package com.example.npcsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.npcsapp.data.local.entities.MarketItemEntity
import com.example.npcsapp.ui.components.PrimaryButton
import com.example.npcsapp.ui.components.SecondaryButton
import com.example.npcsapp.ui.components.nPCsFab
import com.example.npcsapp.ui.components.toPrecio
import com.example.npcsapp.ui.theme.*
import com.example.npcsapp.viewmodel.MarketViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(
    viewModel: MarketViewModel,
    onNavigateToSell: () -> Unit = {},
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToChatList: () -> Unit = {},
    onStartChat: (String, String) -> Unit = { _, _ -> }
) {
    var busqueda by remember { mutableStateOf("") }
    val marketItems by viewModel.marketItems.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title  = { Text("Marketplace", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                actions = {
                    IconButton(onClick = onNavigateToChatList) {
                        Icon(Icons.Default.ChatBubbleOutline, "Mensajes", tint = NeonBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.88f))
            )
        },
        floatingActionButton = {
            nPCsFab(onClick = onNavigateToSell, label = "Publicar artículo")
        },
        containerColor = SurfaceDeep
    ) { padding ->
        LazyVerticalGrid(
            columns               = GridCells.Adaptive(minSize = 300.dp),
            contentPadding        = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement   = Arrangement.spacedBy(16.dp),
            modifier              = Modifier.padding(padding)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(
                        "Hardware verificado de la comunidad salvadoreña. Compra y vende con confianza.",
                        style    = MaterialTheme.typography.bodyMedium,
                        color    = OnSurfaceVariant,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value         = busqueda,
                        onValueChange = { busqueda = it },
                        placeholder   = { Text("Buscar tarjetas gráficas, placas base...", color = Outline) },
                        leadingIcon   = { Icon(Icons.Default.Search, null, tint = Outline) },
                        singleLine    = true,
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(12.dp),
                        colors        = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor      = NeonCyan,
                            unfocusedBorderColor    = OutlineVariant,
                            focusedTextColor        = OnSurface,
                            unfocusedTextColor      = OnSurface,
                            cursorColor             = NeonCyan,
                            focusedContainerColor   = SurfaceDeep,
                            unfocusedContainerColor = SurfaceDeep
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item { FilterChip("Filtros",               Icons.Default.Tune,        seleccionado = true) }
                        item { FilterChip("Condición: cualquiera", seleccionado = false) }
                        item { FilterChip("Precio: menor primero", seleccionado = false) }
                        item { FilterChip("Ubicación: San Salvador", seleccionado = false) }
                    }
                }
            }

            if (marketItems.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 64.dp), contentAlignment = Alignment.Center) {
                        Text("No hay artículos publicados aún.", color = OnSurfaceVariant)
                    }
                }
            } else {
                items(marketItems.filter {
                    busqueda.isBlank() || it.title.contains(busqueda, ignoreCase = true)
                }) { item ->
                    ItemCard(
                        item = item, 
                        onClick = { onNavigateToDetail(item.id.toString()) },
                        onChatClick = { onStartChat(item.sellerId, item.sellerName) }
                    )
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = Alignment.Center) {
                    SecondaryButton("Ver más resultados", onClick = { /* TODO */ })
                }
            }
        }
    }
}

@Composable
private fun FilterChip(
    etiqueta: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector? = null,
    seleccionado: Boolean
) {
    val colorBorde = if (seleccionado) NeonCyan else Color.White.copy(alpha = 0.05f)
    val colorTexto = if (seleccionado) NeonCyan else OnSurfaceVariant

    Surface(
        color    = SurfaceCard.copy(alpha = 0.7f),
        shape    = RoundedCornerShape(50),
        border   = androidx.compose.foundation.BorderStroke(1.dp, colorBorde),
        modifier = Modifier.height(36.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
            if (icono != null) {
                Icon(icono, null, tint = colorTexto, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
            }
            Text(etiqueta, color = colorTexto, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun ItemCard(
    item: MarketItemEntity, 
    onClick: () -> Unit,
    onChatClick: () -> Unit
) {
    val conditionColor = when(item.condition.uppercase()) {
        "NUEVO" -> StatusNew
        "OFERTA" -> StatusOffer
        else -> StatusUsed
    }

    Card(
        onClick   = onClick,
        modifier  = Modifier.fillMaxWidth().height(440.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = SurfaceCard.copy(alpha = 0.8f)),
        border    = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Black.copy(alpha = 0.3f))) {
                if (item.imageUrl != null) {
                    AsyncImage(item.imageUrl, item.title, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Image, "Sin imagen", tint = OnSurfaceVariant.copy(alpha = 0.2f), modifier = Modifier.size(64.dp))
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .background(conditionColor.copy(alpha = 0.20f), RoundedCornerShape(50))
                        .border(1.dp, conditionColor.copy(alpha = 0.30f), RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        item.condition.uppercase(),
                        color         = conditionColor,
                        fontSize      = 10.sp,
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily    = FontFamily.Monospace
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(36.dp)
                        .background(SurfaceDeep.copy(alpha = 0.6f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FavoriteBorder, "Me gusta", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Text(
                        item.title,
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color      = OnSurface,
                        modifier   = Modifier.weight(1f),
                        maxLines   = 2,
                        overflow   = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        item.price.toPrecio(),
                        color      = NeonBlue,
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = OnSurfaceVariant, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(item.location, color = OnSurfaceVariant, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }

                Spacer(Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(PrimaryContainer.copy(alpha = 0.2f), CircleShape)
                                .border(1.dp, NeonBlue.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, null, tint = NeonBlue, modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(item.sellerName, color = OnSurfaceVariant, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Spacer(Modifier.width(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick  = onChatClick,
                            modifier = Modifier.size(40.dp).border(1.dp, OutlineVariant, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.ChatBubbleOutline, "Mensaje", tint = OnSurfaceVariant, modifier = Modifier.size(18.dp))
                        }
                        PrimaryButton(
                            text     = "Ver detalle",
                            onClick  = onClick,
                            modifier = Modifier.height(40.dp)
                        )
                    }
                }
            }
        }
    }
}
