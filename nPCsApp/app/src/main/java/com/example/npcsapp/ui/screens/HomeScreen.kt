package com.example.npcsapp.ui.screens

import android.R.attr.fontWeight
import android.R.attr.letterSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.npcsapp.data.local.entities.BuildEntity
import com.example.npcsapp.data.local.entities.GPUEntity
import com.example.npcsapp.ui.components.toPrecio
import com.example.npcsapp.ui.theme.*
import com.example.npcsapp.viewmodel.BuildViewModel
import com.example.npcsapp.viewmodel.ComponentViewModel

private data class CategoriaPieza(
    val etiqueta: String,
    val icono: ImageVector,
    val categoriaBusqueda: String
)

private val categorias = listOf(
    CategoriaPieza("Tarjeta Gráfica", Icons.Default.DeveloperBoard, "GPUs"),
    CategoriaPieza("Procesador",      Icons.Default.Memory,         "Procesadores"),
    CategoriaPieza("Placa Base",      Icons.Default.Dashboard,      "Tarjetas madre"),
    CategoriaPieza("Memoria RAM",     Icons.Default.SdCard,         "RAM"),
    CategoriaPieza("Almacenamiento",  Icons.Default.Storage,        "Almacenamiento"),
    CategoriaPieza("Fuente",          Icons.Default.Bolt,           "PSU"),
    CategoriaPieza("Gabinete",        Icons.Default.Inventory2,     "Gabinetes"),
    CategoriaPieza("Refrigeración",   Icons.Default.AcUnit,         "Ventiladores CPU"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    componentViewModel: ComponentViewModel,
    buildViewModel: BuildViewModel,
    onStartBuild: () -> Unit,
    onOpenBuild: (Long) -> Unit,
    onOpenComponent: (String, Int) -> Unit,
    onNavigateToSearch: (String) -> Unit,
    onOpenMarket: () -> Unit,
    onSeeAllBuilds: () -> Unit,
) {
    val builds by buildViewModel.builds.collectAsState()
    val gpus by componentViewModel.gpuList.collectAsState()
    val cargandoGpus = componentViewModel.isLoading

    val buildsRecientes = remember(builds) {
        builds.sortedByDescending { it.createdAt }.take(6)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "nPCs",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = NeonBlue,
                        letterSpacing = 1.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceContainerHigh.copy(alpha = 0.88f)
                )
            )
        },
        containerColor = SurfaceDeep
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // ── Hero ────────────────────────────────────────────────────────
            item { HeroCard(onStartBuild = onStartBuild) }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatPill(Icons.Default.Build, builds.size.toString(), "Tus builds", Modifier.weight(1f))
                }
            }

            // ── Cuadrícula de componentes ───────────────────────────────────
            item { SectionHeader("Elige una pieza") }
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    categorias.chunked(4).forEach { fila ->
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            fila.forEach { cat ->
                                CategoryTile(
                                    categoria = cat,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onNavigateToSearch(cat.categoriaBusqueda) }
                                )
                            }
                        }
                    }
                }
            }

            // ── Builds recientes ────────────────────────────────────────────
            item {
                SectionHeader(
                    titulo = "Tus builds recientes",
                    accion = if (buildsRecientes.isNotEmpty()) "Ver todas" else null,
                    onAccion = onSeeAllBuilds
                )
            }
            if (buildsRecientes.isEmpty()) {
                item { EmptyBuildsHint(onStartBuild) }
            } else {
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(buildsRecientes, key = { it.buildId }) { build ->
                            BuildMiniCard(build) { onOpenBuild(build.buildId) }
                        }
                    }
                }
            }

            // ── GPUs destacadas ─────────────────────────────────────────────
            item {
                SectionHeader(
                    titulo = "GPUs destacadas",
                    accion = "Ver Market",
                    onAccion = onOpenMarket
                )
            }
            when {
                cargandoGpus && gpus.isEmpty() -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NeonCyan, strokeWidth = 2.dp)
                    }
                }
                gpus.isEmpty() -> item {
                    Text(
                        "No hay GPUs disponibles por ahora.",
                        color = OnSurfaceVariant.copy(alpha = 0.6f),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
                    )
                }
                else -> item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(gpus.take(10), key = { it.id }) { gpu ->
                            GpuFeaturedCard(gpu) { onOpenComponent("GPU", gpu.id) }
                        }
                    }
                }
            }

            // ── Banner Market ───────────────────────────────────────────────
            item { MarketBanner(onOpenMarket) }
        }
    }
}

// ───────────────────────────────────────────────────────────────────────────
//  COMPONENTES INTERNOS
// ───────────────────────────────────────────────────────────────────────────

@Composable
private fun HeroCard(onStartBuild: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        PrimaryContainer.copy(alpha = 0.45f),
                        SurfaceCard.copy(alpha = 0.95f)
                    )
                )
            )
            .border(1.dp, NeonBlue.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.30f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    "ENSAMBLA SIN ERRORES",
                    color = NeonCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                "Arma tu PC ideal",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Elige tus componentes, compara precios y verifica la compatibilidad.",
                color = OnSurfaceVariant.copy(alpha = 0.85f),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            Spacer(Modifier.height(18.dp))
            Button(
                onClick = onStartBuild,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonBlue,
                    contentColor = OnPrimary
                ),
                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp)
            ) {
                Icon(Icons.Default.Build, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("INICIAR NUEVA BUILD", fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 0.5.sp)
            }
        }
    }
}

@Composable
private fun StatPill(icono: ImageVector, valor: String, etiqueta: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(GlassBg.copy(alpha = 0.75f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(PrimaryContainer.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, null, tint = NeonBlue, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(valor, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(etiqueta, color = OnSurfaceVariant.copy(alpha = 0.6f), fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun SectionHeader(titulo: String, accion: String? = null, onAccion: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 22.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(titulo, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        if (accion != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onAccion() }
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(accion, color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.width(2.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = NeonCyan, modifier = Modifier.size(14.dp))
            }
        }
    }
}

@Composable
private fun CategoryTile(categoria: CategoriaPieza, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(PrimaryContainer.copy(alpha = 0.14f))
            .border(1.dp, NeonBlue.copy(alpha = 0.40f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                categoria.icono,
                null,
                tint = NeonBlue,
                modifier = Modifier.size(26.dp)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                categoria.etiqueta,
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 11.sp
            )
        }
    }
}

@Composable
private fun BuildMiniCard(build: BuildEntity, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(GlassBg.copy(alpha = 0.75f))
            .border(1.dp, Color.White.copy(alpha = 0.09f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(PrimaryContainer.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Build, null, tint = NeonBlue, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(
                build.name,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Abrir", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.width(2.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = NeonCyan, modifier = Modifier.size(12.dp))
            }
        }
    }
}

@Composable
private fun GpuFeaturedCard(gpu: GPUEntity, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(GlassBg.copy(alpha = 0.80f))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(SurfaceDeep)
            ) {
                AsyncImage(
                    model = gpu.image,
                    contentDescription = gpu.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                )
            }
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)) {
                Text(
                    gpu.chipset?: "",
                    color = NeonCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    gpu.name,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp,
                    modifier = Modifier.height(34.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    gpu.price.toPrecio(),
                    color = NeonBlue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun EmptyBuildsHint(onStartBuild: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(GlassBg.copy(alpha = 0.55f))
            .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(16.dp))
            .clickable { onStartBuild() }
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AddCircle, null, tint = NeonBlue, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(14.dp))
            Column {
                Text("Aún no tienes builds", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("Toca para crear tu primera configuración", color = OnSurfaceVariant.copy(alpha = 0.65f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun MarketBanner(onOpenMarket: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 24.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    listOf(SecondaryContainer.copy(alpha = 0.30f), SurfaceCard.copy(alpha = 0.95f))
                )
            )
            .border(1.dp, NeonCyan.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
            .clickable { onOpenMarket() }
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Market de segunda mano", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Compra y vende componentes con otros usuarios.",
                    color = OnSurfaceVariant.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
            Spacer(Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(NeonCyan.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Storefront, null, tint = NeonCyan, modifier = Modifier.size(22.dp))
            }
        }
    }
}
