package com.example.npcsapp.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.npcsapp.ui.components.displayInfoOf
import com.example.npcsapp.ui.components.specsFor
import com.example.npcsapp.ui.components.subtitleFor
import com.example.npcsapp.ui.components.toPrecio
import com.example.npcsapp.ui.theme.*
import com.example.npcsapp.viewmodel.BuildViewModel
import com.example.npcsapp.viewmodel.ComponentViewModel

// Etiquetas en español de cada especificación técnica
private val ETIQUETAS_ESPEC = mapOf(
    "Chipset"        to "Chipset",
    "Memoria"        to "Memoria (GB)",
    "Frecuencia base" to "Frecuencia base (MHz)",
    "Frecuencia boost" to "Frecuencia boost (MHz)",
    "Color"          to "Color",
    "Longitud"       to "Longitud (mm)",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentDetailScreen(
    componentType: String,
    componentId: Int,
    componentViewModel: ComponentViewModel,
    buildViewModel: BuildViewModel,
    onBack: () -> Unit
) {
    val gpus by componentViewModel.gpuList.collectAsState()
    val cpus by componentViewModel.cpuList.collectAsState()
    val motherboards by componentViewModel.motherboardList.collectAsState()
    val rams by componentViewModel.ramList.collectAsState()
    val storages by componentViewModel.storageList.collectAsState()
    val psus by componentViewModel.psuList.collectAsState()
    val cases by componentViewModel.caseList.collectAsState()
    val cpuCoolers by componentViewModel.cpuCoolerList.collectAsState()

    val entity: Any? = when (componentType) {
        "GPU" -> gpus.find { it.id == componentId }
        "CPU" -> cpus.find { it.id == componentId }
        "Motherboard" -> motherboards.find { it.id == componentId }
        "RAM" -> rams.find { it.id == componentId }
        "Storage" -> storages.find { it.id == componentId }
        "PSU" -> psus.find { it.id == componentId }
        "Case" -> cases.find { it.id == componentId }
        "CPUCooler" -> cpuCoolers.find { it.id == componentId }
        else -> null
    }

    val contexto = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        entity?.let { displayInfoOf(componentType, it).name } ?: componentType,
                        color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 18.sp, maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = NeonBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceContainerHigh.copy(alpha = 0.88f)
                )
            )
        },
        containerColor = SurfaceDeep
    ) { padding ->

        if (entity == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Componente no encontrado", color = OnSurfaceVariant)
            }
            return@Scaffold
        }

        val display = displayInfoOf(componentType, entity)
        val specs = specsFor(componentType, entity)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Imagen principal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(SurfaceCard)
            ) {
                AsyncImage(
                    model = display.image,
                    contentDescription = display.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                )
                // Chipset badge inferior izquierdo
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .background(SurfaceDeep.copy(alpha = 0.75f), RoundedCornerShape(8.dp))
                        .border(1.dp, NeonCyan.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        subtitleFor(componentType, entity).uppercase(),
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {

                // Nombre y precio
                Text(display.name, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(4.dp))

                Text(
                    display.price.toPrecio(),
                    color      = NeonBlue,
                    fontSize   = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(Modifier.height(24.dp))

                // Ficha técnica
                Text(
                    "ESPECIFICACIONES",
                    color         = OnSurfaceVariant.copy(alpha = 0.6f),
                    fontSize      = 11.sp,
                    fontWeight    = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp,
                    modifier      = Modifier.padding(bottom = 10.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceCard.copy(alpha = 0.80f))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                ) {
                    Column {
                        specs.forEachIndexed { index, (label, value) ->
                            FilaEspec(label, value)
                            if (index != specs.lastIndex) HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                        }
                    }
                }

                Spacer(Modifier.height(28.dp))

                // Botón Amazon
                if (!display.amazonLink.isNullOrBlank()) {
                    Button(
                        onClick = {
                            contexto.startActivity(Intent(Intent.ACTION_VIEW, display.amazonLink.toUri()))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9900))
                    ) {
                        Text("Comprar en Amazon", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(12.dp))
                }

                // Botón agregar al build
                Button(
                    onClick  = { buildViewModel.addComponentToBuild(componentType, entity) },
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = NeonBlue)
                ) {
                    Text("Agregar al build", color = OnPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Fila de especificación

@Composable
private fun FilaEspec(etiqueta: String, valor: String) {
    Row(
        modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(etiqueta, color = OnSurfaceVariant, fontSize = 13.sp)
        Text(
            valor,
            color      = Color.White,
            fontSize   = 13.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Monospace
        )
    }
}
