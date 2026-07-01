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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.npcsapp.data.local.entities.BuildComponentEntity
import com.example.npcsapp.data.local.entities.GPUEntity
import com.example.npcsapp.ui.theme.*
import com.example.npcsapp.viewmodel.BuildViewModel
import com.example.npcsapp.viewmodel.GPUViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildDetailScreen(
    buildId: Long,
    buildViewModel: BuildViewModel,
    gpuViewModel: GPUViewModel,
    onNavigateToComponentDetail: (Long) -> Unit,
    onNavigateToGpuDetail: (Int) -> Unit,
    onBack: () -> Unit
) {
    val components by buildViewModel.activeBuildComponents.collectAsState()
    val gpus by gpuViewModel.gpuList.collectAsState()

    val gpuComponent = components.firstOrNull { it.componentType == "GPU" }
    val selectedGpu = gpuComponent?.let { comp -> gpus.find { it.id == comp.componentId } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("System Builder", style = MaterialTheme.typography.titleLarge, color = NeonBlue) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = NeonBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDeep.copy(alpha = 0.9f))
            )
        },
        containerColor = SurfaceDeep,
        bottomBar = {
            Box(modifier = Modifier.padding(16.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Costo Estimado", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                            Text(
                                "$${selectedGpu?.price ?: "0.00"}",
                                color = NeonBlue,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                        Button(
                            onClick = { /* Share */ },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonBlue),
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp), tint = OnPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Publicar", color = OnPrimary, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Configura tu máquina de alto rendimiento con validación en tiempo real.",
                    color = OnSurfaceVariant,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // GPU Slot (Lleno si hay GPU, Vacío si no)
            item {
                if (selectedGpu != null && gpuComponent != null) {
                    FilledComponentSlot(
                        type = "GPU",
                        name = selectedGpu.name,
                        price = selectedGpu.price,
                        imageUrl = selectedGpu.image,
                        onClick = { onNavigateToGpuDetail(selectedGpu.id) },
                        onDelete = { buildViewModel.removeComponent(gpuComponent) }
                    )
                } else {
                    EmptyDashedSlot(type = "GPU", subtitle = "Selecciona tu Tarjeta Gráfica") {
                        onNavigateToComponentDetail(1L)
                    }
                }
            }

            // Slots futuros vacíos
            val futureSlots = listOf(
                "CPU" to "Selecciona un procesador",
                "Motherboard" to "Placa base compatible",
                "RAM" to "Elige velocidad y capacidad",
                "Storage" to "Almacenamiento NVMe/SSD",
                "PSU" to "Fuente de poder confiable"
            )

            items(futureSlots) { (type, subtitle) ->
                EmptyDashedSlot(type = type, subtitle = subtitle) { /* TODO */ }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) } // Espacio para el BottomBar
        }
    }
}

// COMPONENTE: Slot Lleno (Glassmorphism)
@Composable
fun FilledComponentSlot(
    type: String, name: String, price: Float, imageUrl: String,
    onClick: () -> Unit, onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard.copy(alpha = 0.8f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceDeep)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(type, color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(name, color = OnSurface, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier
                        .background(StatusNew.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)) {
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text("$$price", color = NeonBlue, fontWeight = FontWeight.Bold)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = OutlineVariant)
            }
        }
    }
}

// COMPONENTE: Slot Vacío (Borde punteado)
@Composable
fun EmptyDashedSlot(type: String, subtitle: String, onClick: () -> Unit) {
    val stroke = Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .drawBehind {
                drawRoundRect(
                    color = OutlineVariant,
                    style = stroke,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx())
                )
            }
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .border(1.dp, OutlineVariant, CircleShape)
                    .background(SurfaceDeep, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = NeonCyan)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(type, color = OnSurfaceVariant, fontWeight = FontWeight.Bold, letterSpacing = 2.sp, fontSize = 14.sp)
            Text(subtitle, color = OutlineVariant, fontSize = 12.sp)
        }
    }
}
