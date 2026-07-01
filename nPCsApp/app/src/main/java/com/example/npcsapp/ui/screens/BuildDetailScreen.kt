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
import com.example.npcsapp.ui.components.componentSlots
import com.example.npcsapp.ui.components.displayInfoOf
import com.example.npcsapp.ui.theme.*
import com.example.npcsapp.viewmodel.BuildViewModel
import com.example.npcsapp.viewmodel.ComponentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildDetailScreen(
    buildId: Long,
    buildViewModel: BuildViewModel,
    componentViewModel: ComponentViewModel,
    onNavigateToComponentSelect: (String) -> Unit,
    onNavigateToComponentDetail: (String, Int) -> Unit,
    onBack: () -> Unit
) {
    val components by buildViewModel.activeBuildComponents.collectAsState()
    val gpus by componentViewModel.gpuList.collectAsState()
    val cpus by componentViewModel.cpuList.collectAsState()
    val motherboards by componentViewModel.motherboardList.collectAsState()
    val rams by componentViewModel.ramList.collectAsState()
    val storages by componentViewModel.storageList.collectAsState()
    val psus by componentViewModel.psuList.collectAsState()
    val cases by componentViewModel.caseList.collectAsState()
    val cpuCoolers by componentViewModel.cpuCoolerList.collectAsState()

    fun findEntity(type: String, id: Int): Any? = when (type) {
        "GPU" -> gpus.find { it.id == id }
        "CPU" -> cpus.find { it.id == id }
        "Motherboard" -> motherboards.find { it.id == id }
        "RAM" -> rams.find { it.id == id }
        "Storage" -> storages.find { it.id == id }
        "PSU" -> psus.find { it.id == id }
        "Case" -> cases.find { it.id == id }
        "CPUCooler" -> cpuCoolers.find { it.id == id }
        else -> null
    }

    val slotDisplays = componentSlots.map { (type, subtitle) ->
        val comp = components.firstOrNull { it.componentType == type }
        val entity = comp?.let { findEntity(type, it.componentId) }
        Triple(type, subtitle, if (comp != null && entity != null) comp to displayInfoOf(type, entity) else null)
    }

    val totalCost = slotDisplays.sumOf { (_, _, filled) -> (filled?.second?.price ?: 0f).toDouble() }.toFloat()


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
                            Text("$${"%.2f".format(totalCost)}", color = NeonBlue, style = MaterialTheme.typography.headlineMedium)
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

            items(slotDisplays) { (type, subtitle, filled) ->
                if (filled != null) {
                    val (comp, display) = filled
                    FilledComponentSlot(
                        type = type,
                        name = display.name,
                        price = display.price,
                        imageUrl = display.image?: "",
                        onClick = { onNavigateToComponentDetail(type, display.id) },
                        onDelete = { buildViewModel.removeComponent(comp) }
                    )
                } else {
                    EmptyDashedSlot(type = type, subtitle = subtitle) {
                        onNavigateToComponentSelect(type)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }// Espacio para el BottomBar
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
