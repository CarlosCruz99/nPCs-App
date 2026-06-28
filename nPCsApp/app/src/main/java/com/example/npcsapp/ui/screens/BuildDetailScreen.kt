package com.example.npcsapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.npcsapp.data.local.entities.BuildComponentEntity
import com.example.npcsapp.data.local.entities.BuildEntity
import com.example.npcsapp.data.local.entities.GPUEntity
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
){
    val components by buildViewModel.activeBuildComponents.collectAsState()
    val gpus by gpuViewModel.gpuList.collectAsState()

    val gpuComponent = components.firstOrNull { it.componentType == "GPU" }
    val selectedGpu = gpuComponent?.let { comp -> gpus.find { it.id == comp.componentId } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles de la build")},
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }

    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item{
                PcComponentSection(
                    componentType = "GPU",
                    componentId = 1L,
                    gpuComponent = gpuComponent,
                    selectedGpu = selectedGpu,
                    onNavigateToComponentDetail = onNavigateToComponentDetail,
                    onNavigateToGpuDetail = onNavigateToGpuDetail,
                    onRemove = { buildViewModel.removeComponent(it) }
                )
            }

        }
    }
}

@Composable
fun PcComponentSection(
    componentType: String,
    componentId: Long,
    gpuComponent: BuildComponentEntity?,
    selectedGpu: GPUEntity?,
    onNavigateToComponentDetail: (Long) -> Unit,
    onNavigateToGpuDetail: (Int) -> Unit,
    onRemove: (BuildComponentEntity) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        if (selectedGpu != null && gpuComponent != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToGpuDetail(selectedGpu.id) }
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = selectedGpu.image,
                    contentDescription = selectedGpu.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(60.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = componentType,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = selectedGpu.name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "$${selectedGpu.price} · ${selectedGpu.memory} GB ${selectedGpu.chipset}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${selectedGpu.coreClock} MHz / ${selectedGpu.coreBoost} MHz boost",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column {
                    IconButton(onClick = { onNavigateToComponentDetail(componentId) }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Cambiar $componentType",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(onClick = { onRemove(gpuComponent) }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Quitar $componentType",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }

            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToComponentDetail(componentId) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar componente"
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Agregar $componentType",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}