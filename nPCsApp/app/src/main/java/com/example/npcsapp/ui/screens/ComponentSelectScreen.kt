package com.example.npcsapp.ui.screens

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
    onBack: () -> Unit
){
    val gpus by gpuViewModel.gpuList.collectAsState()
    val activeComponents by buildViewModel.activeBuildComponents.collectAsState()
    val isLoading = gpuViewModel.isLoading
    val activeBuildId by buildViewModel.activeBuildId.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (componentId){
            1L -> when {
                isLoading -> CircularProgressIndicator()
                gpus.isEmpty() -> Text("No GPUs found")
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        items(gpus) { gpu ->
                            val alreadyInBuild = activeComponents.any {
                                it.componentType == "GPU" && it.componentId == gpu.id
                            }
                            GPUCard(
                                gpu = gpu,
                                alreadyInBuild = alreadyInBuild,
                                canAdd = activeBuildId != null,
                                onAddToBuild = { buildViewModel.addGPUToBuild(gpu.toGPU()) }
                            )
                        }
                    }
                }
            }

        }
    }



}

@Composable
fun ComponentList(){

}


@Composable
fun GPUCard(
    gpu: GPUEntity,
    alreadyInBuild: Boolean,
    canAdd: Boolean,
    onAddToBuild: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model=gpu.image,
                contentDescription = gpu.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(30.dp),
                placeholder = ColorPainter(Color.DarkGray)
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
            when {
                alreadyInBuild -> Text("✓ Added", color = MaterialTheme.colorScheme.primary)
                canAdd -> OutlinedButton(onClick = onAddToBuild) { Text("Add") }
                else -> OutlinedButton(onClick = {}, enabled = false) { Text("Add") }
            }
        }
    }
}

// Extension to convert GPUEntity back to GPU domain model
fun GPUEntity.toGPU() = GPU(
    id = id, name = name, price = price, chipset = chipset,
    memory = memory, coreClock = coreClock, coreBoost = coreBoost,
    color = color, length = length, image = image, amazonLink = amazonLink
)