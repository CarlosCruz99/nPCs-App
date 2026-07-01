package com.example.npcsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.npcsapp.ui.components.PrimaryButton
import com.example.npcsapp.ui.components.nPCsFab
import com.example.npcsapp.ui.components.toPrecioCorto
import com.example.npcsapp.ui.theme.*
import com.example.npcsapp.viewmodel.BuildViewModel
import com.example.npcsapp.viewmodel.GPUViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private enum class BuildTab { MIS_BUILDS, COMUNIDAD }

// ── Modelo de build de comunidad — sin campo de etiqueta de estado ────────
private data class BuildComunidad(
    val titulo: String, val autor: String, val precio: Float,
    val valoracion: String, val cpu: String, val ram: String, val gpu: String,
    val likes: Int, val imagenUrl: String,
)

private val buildsComunidad = listOf(
    BuildComunidad("Cuscatlán Cyber-Rig", "@GamerSV_99", 2450f, "4.9",
        "Ryzen 9 7900X", "32 GB DDR5", "RTX 4080 Super", 124, "https://images.unsplash.com/photo-1587202372634-32705e3bf49c?w=800"),
    BuildComunidad("San Salvador Edit Station", "@PixelMaster_SV", 1890f, "4.7",
        "Core i7-14700K", "64 GB DDR5", "RTX 4070 Ti", 86, "https://images.unsplash.com/photo-1620712943543-bcc4688e7485?w=800"),
    BuildComunidad("Santa Ana Budget King", "@EcoBuilds_SV", 850f, "4.5",
        "Ryzen 5 5600", "16 GB DDR4", "RTX 3060", 215, "https://images.unsplash.com/photo-1593640408182-31c228d52c7c?w=800"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildsScreen(
    gpuViewModel: GPUViewModel,
    buildViewModel: BuildViewModel,
    onNavigateToDetail: (Long) -> Unit
) {
    val builds by buildViewModel.builds.collectAsState()
    var tabActivo        by remember { mutableStateOf(BuildTab.MIS_BUILDS) }
    var busqueda         by remember { mutableStateOf("") }
    var mostrarDialogo   by remember { mutableStateOf(false) }
    var nombreNuevaBuild by remember { mutableStateOf("") }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest  = { mostrarDialogo = false },
            containerColor    = GlassBg,
            titleContentColor = NeonBlue,
            title = { Text("Nueva Build", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = nombreNuevaBuild, onValueChange = { nombreNuevaBuild = it },
                    label = { Text("Nombre de la build") }, singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan, unfocusedBorderColor = OutlineVariant,
                        focusedLabelColor = NeonCyan, focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White, cursorColor = NeonCyan,
                    ), modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nombreNuevaBuild.isNotBlank()) {
                            buildViewModel.createBuild(nombreNuevaBuild.trim())
                            nombreNuevaBuild = ""; mostrarDialogo = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonBlue)
                ) { Text("Crear", color = OnPrimary, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar", color = OnSurfaceVariant)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Builds", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = NeonBlue, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                colors         = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.88f))
            )
        },
        floatingActionButton = {
            nPCsFab(onClick = { mostrarDialogo = true }, label = "Nueva Build")
        },
        containerColor = SurfaceDeep
    ) { paddingValues ->
        LazyColumn(
            modifier       = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(bottom = 100.dp),
        ) {
            item {
                OutlinedTextField(
                    value = busqueda, onValueChange = { busqueda = it },
                    placeholder = { Text("Buscar ensambles maestros...", color = OnSurfaceVariant.copy(alpha = 0.55f), fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = Outline) },
                    singleLine = true, shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan, unfocusedBorderColor = OutlineVariant,
                        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                        cursorColor = NeonCyan, focusedContainerColor = SurfaceDeep, unfocusedContainerColor = SurfaceDeep
                    ),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
            item {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()).padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TabChip("Mis Builds", tabActivo == BuildTab.MIS_BUILDS) { tabActivo = BuildTab.MIS_BUILDS }
                    TabChip("Comunidad",  tabActivo == BuildTab.COMUNIDAD)  { tabActivo = BuildTab.COMUNIDAD }
                }
            }
            when (tabActivo) {
                BuildTab.MIS_BUILDS -> {
                    val filtradas = builds.filter { busqueda.isBlank() || it.name.contains(busqueda, ignoreCase = true) }
                    if (filtradas.isEmpty()) {
                        item { EstadoSinBuilds(onCrear = { mostrarDialogo = true }) }
                    } else {
                        items(filtradas, key = { it.buildId }) { build ->
                            MiBuildCard(build, { buildViewModel.deleteBuild(it) }, { onNavigateToDetail(build.buildId) }, Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
                        }
                    }
                }
                BuildTab.COMUNIDAD -> {
                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            buildsComunidad.forEach { BuildComunidadCard(it) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabChip(etiqueta: String, seleccionado: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (seleccionado) PrimaryContainer.copy(alpha = 0.22f) else SurfaceContainerHigh)
            .border(1.dp, if (seleccionado) NeonBlue.copy(alpha = 0.50f) else Color.White.copy(alpha = 0.06f), CircleShape)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 9.dp)
    ) {
        Text(etiqueta, color = if (seleccionado) NeonBlue else OnSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun MiBuildCard(build: BuildEntity, onEliminar: (BuildEntity) -> Unit, onVerDetalle: () -> Unit, modifier: Modifier = Modifier) {
    val fecha = remember(build.createdAt) { SimpleDateFormat("dd MMM yyyy", Locale("es", "SV")).format(Date(build.createdAt)) }
    Box(
        modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
            .background(GlassBg.copy(alpha = 0.75f))
            .border(1.dp, Color.White.copy(alpha = 0.09f), RoundedCornerShape(16.dp))
            .clickable { onVerDetalle() }
    ) {
        Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(PrimaryContainer.copy(alpha = 0.18f)).border(1.dp, NeonBlue.copy(alpha = 0.25f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Build, null, tint = NeonBlue, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(build.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(3.dp))
                Text("Creada el $fecha", color = OnSurfaceVariant.copy(alpha = 0.7f), fontSize = 12.sp)
            }
            Spacer(Modifier.width(8.dp))
            Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFFFB4AB).copy(alpha = 0.10f)).clickable { onEliminar(build) }, contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Delete, "Eliminar", tint = Color(0xFFFFB4AB), modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun BuildComunidadCard(build: BuildComunidad) {
    var likeActivo by remember { mutableStateOf(false) }
    var totalLikes by remember { mutableIntStateOf(build.likes) }

    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(GlassBg.copy(alpha = 0.80f)).border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(16.dp))) {
        Column {
            // ── Imagen hero — sin etiqueta de estado, solo valoración ──────
            Box(modifier = Modifier.fillMaxWidth().height(220.dp)) {
                AsyncImage(build.imagenUrl, build.titulo, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                Box(modifier = Modifier.fillMaxWidth().height(80.dp).align(Alignment.BottomCenter).background(Brush.verticalGradient(listOf(Color.Transparent, GlassBg.copy(alpha = 0.90f)))))
                Box(modifier = Modifier.padding(12.dp).align(Alignment.BottomEnd).clip(RoundedCornerShape(8.dp)).background(Color.Black.copy(alpha = 0.60f)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(3.dp))
                        Text(build.valoracion, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(build.titulo, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(Modifier.height(4.dp))
                        Box(modifier = Modifier.clip(CircleShape).background(SurfaceContainerHighest).padding(horizontal = 10.dp, vertical = 3.dp)) {
                            Text(build.autor, color = OnSurfaceVariant, fontSize = 11.sp)
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(horizontalAlignment = Alignment.End) {
                        Text(build.precio.toPrecioCorto(), color = NeonBlue, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        Text("ESTIMADO", color = OnSurfaceVariant.copy(alpha = 0.55f), fontSize = 9.sp, letterSpacing = 0.5.sp)
                    }
                }
                Spacer(Modifier.height(14.dp))
                // ── Ficha de especificaciones con íconos (sin emojis) ───────
                Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(SurfaceDeep).border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(10.dp)).padding(vertical = 10.dp, horizontal = 8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        ColumnaEspec(Icons.Default.Memory,         "CPU", build.cpu); DivisorEspec()
                        ColumnaEspec(Icons.Default.SdCard,         "RAM", build.ram); DivisorEspec()
                        ColumnaEspec(Icons.Default.DeveloperBoard, "GPU", build.gpu)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable { likeActivo = !likeActivo; totalLikes = if (likeActivo) build.likes + 1 else build.likes }.padding(horizontal = 10.dp, vertical = 6.dp)) {
                        Icon(Icons.Default.Favorite, "Me gusta", tint = if (likeActivo) Color(0xFFEC4899) else OnSurfaceVariant, modifier = Modifier.size(20.dp))
                        Text("$totalLikes", color = if (likeActivo) Color(0xFFEC4899) else OnSurfaceVariant, fontSize = 13.sp)
                    }
                    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable {}.padding(8.dp)) {
                        Icon(Icons.Default.Share, "Compartir", tint = OnSurfaceVariant, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnaEspec(icono: ImageVector, etiqueta: String, valor: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icono, null, tint = NeonCyan, modifier = Modifier.size(18.dp))
        Spacer(Modifier.height(4.dp))
        Text(etiqueta, color = OnSurfaceVariant.copy(alpha = 0.65f), fontSize = 9.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp)
        Spacer(Modifier.height(2.dp))
        Text(valor, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun DivisorEspec() {
    Box(modifier = Modifier.width(1.dp).height(36.dp).background(Color.White.copy(alpha = 0.08f)))
}

@Composable
private fun EstadoSinBuilds(onCrear: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 72.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(modifier = Modifier.size(72.dp).clip(CircleShape).background(PrimaryContainer.copy(alpha = 0.14f)).border(1.dp, NeonBlue.copy(alpha = 0.22f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Build, null, tint = NeonBlue.copy(alpha = 0.6f), modifier = Modifier.size(32.dp))
        }
        Text("Sin builds todavía", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Text("Toca + para crear tu primera configuración", color = OnSurfaceVariant.copy(alpha = 0.65f), fontSize = 13.sp)
        PrimaryButton("Nueva Build", onCrear, Modifier.padding(horizontal = 32.dp))
    }
}