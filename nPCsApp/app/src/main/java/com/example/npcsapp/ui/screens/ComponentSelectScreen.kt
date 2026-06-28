package com.example.npcsapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.npcsapp.data.GPU
import com.example.npcsapp.data.local.entities.GPUEntity
import com.example.npcsapp.viewmodel.BuildViewModel
import com.example.npcsapp.viewmodel.GPUViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentSelectScreen(
    componentId: Long,
    gpuViewModel: GPUViewModel,
    buildViewModel: BuildViewModel,
    onNavigateToGpuDetail: (Int) -> Unit,
    onBack: () -> Unit
){
    val gpus by gpuViewModel.gpuList.collectAsState()
    val activeComponents by buildViewModel.activeBuildComponents.collectAsState()
    val isLoading = gpuViewModel.isLoading
    val activeBuildId by buildViewModel.activeBuildId.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seleccionar GPU") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retornar")
                    }
                },
                actions = {
                    IconButton(onClick = { gpuViewModel.refreshGPUs() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recargar GPUs")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (componentId){
            1L -> {
                if (isLoading) {
                    Column(
                        modifier = Modifier.padding(paddingValues).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (gpus.isEmpty()) {
                    Text("No se encontraron GPUs", modifier = Modifier.padding(paddingValues))
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(paddingValues).padding(horizontal = 16.dp)
                    ) {
                        items(gpus) { gpu ->
                            val alreadyInBuild = activeComponents.any {
                                it.componentType == "GPU" && it.componentId == gpu.id
                            }
                            GPUCard(
                                gpu = gpu,
                                alreadyInBuild = alreadyInBuild,
                                canAdd = activeBuildId != null,
                                onAddToBuild = {
                                    buildViewModel.addGPUToBuild(gpu.toGPU())
                                    onBack()
                                },
                                onNavigateToDetail = { onNavigateToGpuDetail(gpu.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GPUCard(
    gpu: GPUEntity,
    alreadyInBuild: Boolean,
    canAdd: Boolean,
    onAddToBuild: () -> Unit,
    onNavigateToDetail: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable { onNavigateToDetail() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model=gpu.image,
                contentDescription = gpu.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(50.dp),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(gpu.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    "$${gpu.price} · ${gpu.memory} GB ${gpu.chipset}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            when {
                alreadyInBuild -> Text("Agregado", color = MaterialTheme.colorScheme.primary)
                canAdd -> OutlinedButton(onClick = onAddToBuild) { Text("Agregar") }
                else -> OutlinedButton(onClick = {}, enabled = false) { Text("Agregar") }
            }
        }
    }
}

fun GPUEntity.toGPU() = GPU(
    id = id, name = name, price = price, chipset = chipset,
    memory = memory, coreClock = coreClock, coreBoost = coreBoost,
    color = color, length = length, image = image, amazonLink = amazonLink
)
