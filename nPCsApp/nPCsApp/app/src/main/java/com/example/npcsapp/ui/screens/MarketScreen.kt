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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.npcsapp.ui.components.PrimaryButton
import com.example.npcsapp.ui.components.SecondaryButton
import com.example.npcsapp.ui.components.nPCsFab
import com.example.npcsapp.ui.components.toPrecio
import com.example.npcsapp.ui.theme.*

data class MarketItem(
    val id: String,
    val titulo: String,
    val precio: Float,
    val ubicacion: String,
    val vendedor: String,
    val estadoTexto: String,
    val estadoColor: Color,
    val imagenUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(
    onNavigateToSell: () -> Unit = {},
    onNavigateToDetail: (String) -> Unit = {}
) {
    var busqueda by remember { mutableStateOf("") }

    val articulos = listOf(
        MarketItem("1", "EVGA RTX 3080 FTW3 Ultra",  550f,  "San Salvador, Escalón",  "Roberto M.",   "Usado - Excelente", StatusUsed,  "https://m.media-amazon.com/images/I/81sXFTXt5CS._AC_SX466_.jpg"),
        MarketItem("2", "AMD Ryzen 9 5900X",           285f,  "Santa Ana, Centro",       "HardwareSV",   "Caja abierta",      StatusNew,   "https://m.media-amazon.com/images/I/71klxIIJP5L._AC_SL1500_.jpg"),
        MarketItem("3", "32 GB Trident Z Neo 3600",   110f,  "San Miguel",              "Techie99",     "Usado - Bueno",     StatusUsed,  "https://m.media-amazon.com/images/I/61l4EStxhnL._AC_SL1274_.jpg"),
        MarketItem("4", "Corsair RM1000x 1000 W",      95f,  "La Libertad",             "GamerGuy_SV",  "Usado - Regular",   StatusOffer, "https://m.media-amazon.com/images/I/71Xai-xG7wL._AC_SX466_.jpg")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title  = { Text("Marketplace", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.88f))
            )
        },
        // ── FAB unificado (mismo estilo que BuildsScreen) ─────────────────
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
            // ── Encabezado y barra de búsqueda ────────────────────────────
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
                    // Chips de filtro
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item { FilterChip("Filtros",               Icons.Default.Tune,        seleccionado = true) }
                        item { FilterChip("Condición: cualquiera", seleccionado = false) }
                        item { FilterChip("Precio: menor primero", seleccionado = false) }
                        item { FilterChip("Ubicación: San Salvador", seleccionado = false) }
                    }
                }
            }

            // ── Cuadrícula de artículos ───────────────────────────────────
            items(articulos.filter {
                busqueda.isBlank() || it.titulo.contains(busqueda, ignoreCase = true)
            }) { item ->
                ItemCard(item = item, onClick = { onNavigateToDetail(item.id) })
            }

            // ── Cargar más ────────────────────────────────────────────────
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = Alignment.Center) {
                    SecondaryButton("Ver más resultados", onClick = { /* TODO */ })
                }
            }
        }
    }
}

// ── Chip de filtro ────────────────────────────────────────────────────────

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

// ── Tarjeta de artículo ───────────────────────────────────────────────────

@Composable
private fun ItemCard(item: MarketItem, onClick: () -> Unit) {
    Card(
        onClick   = onClick,
        modifier  = Modifier.fillMaxWidth().height(420.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = SurfaceCard.copy(alpha = 0.8f)),
        border    = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Imagen y badges ───────────────────────────────────────────
            Box(modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Black.copy(alpha = 0.3f))) {
                AsyncImage(item.imagenUrl, item.titulo, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())

                // Badge de estado (Usado / Nuevo / Oferta)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .background(item.estadoColor.copy(alpha = 0.20f), RoundedCornerShape(50))
                        .border(1.dp, item.estadoColor.copy(alpha = 0.30f), RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        item.estadoTexto.uppercase(),
                        color         = item.estadoColor,
                        fontSize      = 10.sp,
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily    = FontFamily.Monospace
                    )
                }

                // Botón favorito
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

            // ── Detalles ──────────────────────────────────────────────────
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Text(
                        item.titulo,
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color      = OnSurface,
                        modifier   = Modifier.weight(1f),
                        maxLines   = 2
                    )
                    Spacer(Modifier.width(8.dp))
                    // Precio formateado con formato unificado
                    Text(
                        item.precio.toPrecio(),
                        color      = NeonBlue,
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Ubicación
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = OnSurfaceVariant, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(item.ubicacion, color = OnSurfaceVariant, fontSize = 13.sp)
                }

                Spacer(Modifier.height(16.dp))

                // Vendedor y acciones
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
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
                        Text(item.vendedor, color = OnSurfaceVariant, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Botón de chat — mismo borde que en BuildDetailScreen
                        IconButton(
                            onClick  = { /* chat — pendiente */ },
                            modifier = Modifier.size(36.dp).border(1.dp, OutlineVariant, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.ChatBubbleOutline, "Mensaje", tint = OnSurfaceVariant, modifier = Modifier.size(18.dp))
                        }
                        // Botón primario unificado
                        PrimaryButton(
                            text     = "Ver detalle",
                            onClick  = onClick,
                            modifier = Modifier.height(36.dp)
                        )
                    }
                }
            }
        }
    }
}