package com.example.npcsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SdCard
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.npcsapp.data.GPU
import com.example.npcsapp.data.local.entities.GPUEntity
import com.example.npcsapp.ui.theme.GlassBg
import com.example.npcsapp.ui.theme.NeonBlue
import com.example.npcsapp.ui.theme.NeonCyan
import com.example.npcsapp.ui.theme.OnSurfaceVariant
import com.example.npcsapp.ui.theme.Outline
import com.example.npcsapp.ui.theme.OutlineVariant
import com.example.npcsapp.ui.theme.PrimaryContainer
import com.example.npcsapp.ui.theme.StatusNew
import com.example.npcsapp.ui.theme.StatusOffer
import com.example.npcsapp.ui.theme.SurfaceBase
import com.example.npcsapp.ui.theme.SurfaceContainerHigh
import com.example.npcsapp.ui.theme.SurfaceDeep
import com.example.npcsapp.viewmodel.BuildViewModel
import com.example.npcsapp.viewmodel.GPUViewModel

// ── Filter chip data ──────────────────────────────────────────────────────

private data class FilterChip(val label: String, val icon: ImageVector)

private val filterChips = listOf(
    FilterChip("GPUs",       Icons.Default.DeveloperBoard),
    FilterChip("Processors", Icons.Default.Memory),
    FilterChip("RAM",        Icons.Default.SdCard),
    FilterChip("Storage",    Icons.Default.Storage),
    FilterChip("Más",        Icons.Default.MoreHoriz),
)

// ── Screen ────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentSelectScreen(
    componentId: Long,
    gpuViewModel: GPUViewModel,
    buildViewModel: BuildViewModel,
    onNavigateToGpuDetail: (Int) -> Unit,
    onBack: () -> Unit
) {
    val gpus            by gpuViewModel.gpuList.collectAsState()
    val activeComponents by buildViewModel.activeBuildComponents.collectAsState()
    val activeBuildId   by buildViewModel.activeBuildId.collectAsState()
    val isLoading       = gpuViewModel.isLoading

    var query           by remember { mutableStateOf("") }

    // 1. Mapeamos el componentId al nombre del chip que necesitamos
    val targetChipLabel = when (componentId) {
        1L -> "GPUs"
        2L -> "Processors"
        3L -> "RAM"
        4L -> "Storage"
        else -> "Más"
    }

    // 2. Filtramos la lista original para dejar solo el chip relevante
    val activeChips = remember(componentId) {
        filterChips.filter { it.label == targetChipLabel }
    }

    // 3. Establecemos el filtro activo por defecto al chip seleccionado
    var activeFilter    by remember { mutableStateOf(targetChipLabel) }

    // Simple client-side filter by name
    val filtered = remember(gpus, query) {
        if (query.isBlank()) gpus
        else gpus.filter { it.name.contains(query, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text       = "nPCs",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 22.sp,
                        color      = NeonBlue
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint               = NeonBlue
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { gpuViewModel.refreshGPUs() }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Recargar",
                            tint               = NeonBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceContainerHigh.copy(alpha = 0.85f)
                )
            )
        },
        containerColor = SurfaceDeep
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // ── Search bar ────────────────────────────────────────────────
            OutlinedTextField(
                value         = query,
                onValueChange = { query = it },
                placeholder   = {
                    Text(
                        "Buscar componentes, benchmarks...",
                        color = OnSurfaceVariant.copy(alpha = 0.6f),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Outline)
                },
                singleLine = true,
                shape      = RoundedCornerShape(16.dp),
                colors     = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = NeonCyan,
                    unfocusedBorderColor = OutlineVariant,
                    focusedTextColor     = Color.White,
                    unfocusedTextColor   = Color.White,
                    cursorColor          = NeonCyan,
                    focusedContainerColor   = SurfaceDeep,
                    unfocusedContainerColor = SurfaceDeep
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            // ── Filter chips ────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                activeChips.forEach { chip ->
                    val selected = chip.label == activeFilter
                    Surface(
                        shape  = CircleShape,
                        color  = if (selected) PrimaryContainer.copy(alpha = 0.25f)
                        else SurfaceContainerHigh,
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = if (selected) NeonBlue.copy(alpha = 0.50f)
                            else Color.White.copy(alpha = 0.06f)
                        ),
                        modifier = Modifier.clickable { activeFilter = chip.label }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = chip.icon,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = if (selected) NeonBlue else OnSurfaceVariant
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text     = chip.label,
                                color    = if (selected) NeonBlue else OnSurfaceVariant,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // ── List / states ─────────────────────────────────────────────
            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = NeonBlue)
                    }
                }
                filtered.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint     = OnSurfaceVariant.copy(alpha = 0.4f)
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "No se encontraron componentes",
                                color    = OnSurfaceVariant.copy(alpha = 0.6f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding          = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement     = Arrangement.spacedBy(12.dp),
                        modifier                = Modifier.fillMaxSize()
                    ) {
                        items(filtered) { gpu ->
                            val alreadyInBuild = activeComponents.any {
                                it.componentType == "GPU" && it.componentId == gpu.id
                            }
                            SearchGPUCard(
                                gpu            = gpu,
                                alreadyInBuild = alreadyInBuild,
                                canAdd         = activeBuildId != null,
                                onAddToBuild   = {
                                    buildViewModel.addGPUToBuild(gpu.toGPU())
                                    onBack()
                                },
                                onNavigateToDetail = { onNavigateToGpuDetail(gpu.id) }
                            )
                        }
                        item { Spacer(Modifier.height(8.dp)) }
                    }
                }
            }
        }
    }
}

// ── GPU Card — new design ─────────────────────────────────────────────────

@Composable
fun SearchGPUCard(
    gpu: GPUEntity,
    alreadyInBuild: Boolean,
    canAdd: Boolean,
    onAddToBuild: () -> Unit,
    onNavigateToDetail: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(GlassBg.copy(alpha = 0.75f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.10f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onNavigateToDetail() }
    ) {
        Column {
            // ── Image with badges ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                AsyncImage(
                    model              = gpu.image,
                    contentDescription = gpu.name,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.fillMaxSize()
                )
                // Dark gradient overlay at bottom of image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, GlassBg.copy(alpha = 0.85f))
                            )
                        )
                )
                // Status badge (top-left)
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    StatusBadge(
                        text  = if (alreadyInBuild) "EN BUILD" else "IN STOCK",
                        color = if (alreadyInBuild) NeonBlue else StatusNew
                    )
                }
                // Price badge (bottom-right of image)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                        .background(
                            Color.Black.copy(alpha = 0.55f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text       = "$${gpu.price}",
                        color      = NeonBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 13.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // ── Card body ─────────────────────────────────────────────────
            Column(modifier = Modifier.padding(14.dp)) {
                // Chipset label (secondary accent, monospace)
                Text(
                    text       = gpu.chipset.uppercase(),
                    color      = NeonCyan,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(2.dp))
                // GPU name
                Text(
                    text       = gpu.name,
                    color      = Color.White,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines   = 2
                )
                Spacer(Modifier.height(10.dp))

                // Store price rows (mimicking the HTML comparison table)
                PriceRow(store = "Amazon", price = gpu.price, highlight = gpu.amazonLink != null)
                Spacer(Modifier.height(4.dp))
                PriceRow(store = "${gpu.memory.toInt()} GB VRAM", price = null, spec = "${gpu.coreClock} MHz")

                Spacer(Modifier.height(12.dp))

                // Action button
                when {
                    alreadyInBuild -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    NeonBlue.copy(alpha = 0.12f),
                                    RoundedCornerShape(10.dp)
                                )
                                .border(1.dp, NeonBlue.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "✓  En tu Build",
                                color      = NeonBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 14.sp
                            )
                        }
                    }
                    canAdd -> {
                        Button(
                            onClick  = onAddToBuild,
                            shape    = RoundedCornerShape(10.dp),
                            colors   = ButtonDefaults.buttonColors(
                                containerColor = NeonBlue,
                                contentColor   = Color(0xFF00285D)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Añadir al Build", fontWeight = FontWeight.Bold)
                        }
                    }
                    else -> {
                        Button(
                            onClick  = {},
                            enabled  = false,
                            shape    = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Selecciona un Build primero")
                        }
                    }
                }
            }
        }
    }
}

// ── Small helpers ─────────────────────────────────────────────────────────

@Composable
private fun StatusBadge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.18f), RoundedCornerShape(50))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(
            text       = text,
            color      = color,
            fontSize   = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
    }
}

@Composable
private fun PriceRow(store: String, price: Float?, highlight: Boolean = false, spec: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceDeep, RoundedCornerShape(8.dp))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(store, color = OnSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Text(
            text       = price?.let { "$${"%.2f".format(it)}" } ?: (spec ?: "—"),
            color      = if (price != null) NeonBlue else NeonCyan,
            fontSize   = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}

fun GPUEntity.toGPU() = GPU(
    id = id, name = name, price = price, chipset = chipset,
    memory = memory, coreClock = coreClock, coreBoost = coreBoost,
    color = color, length = length, image = image, amazonLink = amazonLink
)
