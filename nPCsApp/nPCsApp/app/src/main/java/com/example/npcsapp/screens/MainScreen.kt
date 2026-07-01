package com.example.npcsapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.npcsapp.data.GPU
import com.example.npcsapp.data.local.entities.GPUEntity
import com.example.npcsapp.viewmodel.BuildViewModel
import com.example.npcsapp.viewmodel.GPUViewModel

@Composable
fun MainScreen(
    gpuViewModel: GPUViewModel,
    buildViewModel: BuildViewModel,
    modifier: Modifier = Modifier
) {
    val gpus by gpuViewModel.gpuList.collectAsState()
    val builds by buildViewModel.builds.collectAsState()
    val activeBuildId by buildViewModel.activeBuildId.collectAsState()
    val activeComponents by buildViewModel.activeBuildComponents.collectAsState()
    val isLoading = gpuViewModel.isLoading

    var showNewBuildDialog by remember { mutableStateOf(false) }
    var newBuildName by remember { mutableStateOf("") }

    if (showNewBuildDialog) {
        AlertDialog(
            onDismissRequest = { showNewBuildDialog = false },
            title = { Text("New build") },
            text = {
                OutlinedTextField(
                    value = newBuildName,
                    onValueChange = { newBuildName = it },
                    label = { Text("Build name") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newBuildName.isNotBlank()) {
                        buildViewModel.createBuild(newBuildName.trim())
                        newBuildName = ""
                        showNewBuildDialog = false
                    }
                }) { Text("Create") }
            },
            dismissButton = {
                TextButton(onClick = { showNewBuildDialog = false }) { Text("Cancel") }
            }
        )
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // Build selector row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Active build:", style = MaterialTheme.typography.labelLarge)
            if (builds.isEmpty()) {
                Text("None", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        val name = builds.find { it.buildId == activeBuildId }?.name ?: "Select…"
                        Text(name)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        builds.forEach { build ->
                            DropdownMenuItem(
                                text = { Text(build.name) },
                                onClick = {
                                    buildViewModel.selectBuild(build.buildId)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            IconButton(onClick = { showNewBuildDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "New build")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            isLoading -> CircularProgressIndicator()
            gpus.isEmpty() -> Text("No GPUs found")
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
            Column(modifier = Modifier.weight(1f)) {
                Text(gpu.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    "$${gpu.price} · ${gpu.memory}GB ${gpu.chipset}",
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