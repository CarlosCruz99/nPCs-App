package com.example.npcsapp.ui.screens

import android.R.attr.name
import android.R.attr.onClick
import android.R.attr.subtitle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.npcsapp.data.local.entities.*
import com.example.npcsapp.ui.components.toPrecio
import com.example.npcsapp.ui.theme.*
import com.example.npcsapp.viewmodel.ComponentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    componentViewModel: ComponentViewModel,
    initialCategory: String = "GPUs",
    onNavigateToComponentDetail: (String, Int) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf(initialCategory) }
    val categories = listOf("GPUs", "Procesadores", "RAM", "Tarjetas madre", "Almacenamiento", "PSU", "Gabinetes", "Ventiladores CPU")

    val gpus by componentViewModel.gpuList.collectAsState()
    val cpus by componentViewModel.cpuList.collectAsState()
    val motherboards by componentViewModel.motherboardList.collectAsState()
    val rams by componentViewModel.ramList.collectAsState()
    val storages by componentViewModel.storageList.collectAsState()
    val psus by componentViewModel.psuList.collectAsState()
    val cases by componentViewModel.caseList.collectAsState()
    val cpuCoolers by componentViewModel.cpuCoolerList.collectAsState()

    val isLoading = componentViewModel.isLoading

    val filteredGpus = remember(gpus, query) {
        gpus.filter { it.name.contains(query, ignoreCase = true) || (it.chipset?.contains(query, ignoreCase = true) ?: false) }
    }
    val filteredCpus = remember(cpus, query) {
        cpus.filter { it.name.contains(query, ignoreCase = true) || (it.microarchitecture?.contains(query, ignoreCase = true) ?: false) }
    }
    val filteredMotherboards = remember(motherboards, query) {
        motherboards.filter { it.name.contains(query, ignoreCase = true) || (it.socket?.contains(query, ignoreCase = true) ?: false) }
    }
    val filteredRams = remember(rams, query) {
        rams.filter { it.name.contains(query, ignoreCase = true) || (it.moduleMemory?.toString()?.contains(query, ignoreCase = true) ?: false) }
    }
    val filteredStorages = remember(storages, query) {
        storages.filter { it.name.contains(query, ignoreCase = true) || (it.capacity?.toString()?.contains(query, ignoreCase = true) ?: false) }
    }
    val filteredPsus = remember(psus, query) {
        psus.filter { it.name.contains(query, ignoreCase = true) || (it.efficiency?.contains(query, ignoreCase = true) ?: false) }
    }
    val filteredCases = remember(cases, query) {
        cases.filter { it.name.contains(query, ignoreCase = true) || (it.type?.contains(query, ignoreCase = true) ?: false) }
    }
    val filteredCpuCoolers = remember(cpuCoolers, query) {
        cpuCoolers.filter { it.name.contains(query, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Buscar componentes",
                        color = NeonBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        containerColor = SurfaceDeep
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = {
                    Text(
                        text = "Buscar tarjetas gráficas, procesadores...",
                        color = Outline
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Outline
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = OutlineVariant,
                    focusedTextColor = OnSurface,
                    unfocusedTextColor = OnSurface,
                    cursorColor = NeonCyan,
                    focusedContainerColor = SurfaceDeep,
                    unfocusedContainerColor = SurfaceDeep
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NeonCyan.copy(alpha = 0.2f),
                            selectedLabelColor = NeonCyan,
                            labelColor = OnSurfaceVariant
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = OutlineVariant,
                            selectedBorderColor = NeonCyan
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contenido según categoría
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = NeonCyan
                    )
                } else {
                    when (selectedCategory) {
                        "GPUs" -> {
                            if (filteredGpus.isEmpty()) EmptyState()
                            else LazyVerticalGrid(
                                columns = GridCells.Adaptive(170.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(filteredGpus) { gpu ->
                                    CatalogCard(
                                        image = gpu.image?: "",
                                        subtitle = gpu.chipset?.uppercase() ?: "N/D",
                                        name = gpu.name,
                                        price = gpu.price,
                                        onClick = { onNavigateToComponentDetail("GPU", gpu.id) }
                                    )
                                }
                            }
                        }
                        "Procesadores" -> {
                            if (filteredCpus.isEmpty()) EmptyState()
                            else LazyVerticalGrid(
                                columns = GridCells.Adaptive(170.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(filteredCpus) { cpu ->
                                    CatalogCard(
                                        image = cpu.image?: "",
                                        subtitle = cpu.microarchitecture?.uppercase() ?: "N/D",
                                        name = cpu.name,
                                        price = cpu.price,
                                        onClick = { onNavigateToComponentDetail("CPU", cpu.id) }
                                    )
                                }
                            }
                        }
                        "RAM" -> {
                            if (filteredRams.isEmpty()) EmptyState()
                            else LazyVerticalGrid(
                                columns = GridCells.Adaptive(170.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(filteredRams) { ram ->
                                    CatalogCard(
                                        image = ram.image?: "",
                                        subtitle = ram.moduleMemory.toString(),
                                        name = ram.name,
                                        price = ram.price,
                                        onClick = { onNavigateToComponentDetail("RAM", ram.id) }
                                    )
                                }
                            }
                        }
                        "Tarjetas madre" -> {
                            if (filteredMotherboards.isEmpty()) EmptyState()
                            else LazyVerticalGrid(
                                columns = GridCells.Adaptive(170.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(filteredMotherboards) { mb ->
                                    CatalogCard(
                                        image = mb.image?: "",
                                        subtitle = mb.socket?.uppercase() ?: "N/D",
                                        name = mb.name,
                                        price = mb.price,
                                        onClick = { onNavigateToComponentDetail("Motherboard", mb.id) }
                                    )
                                }
                            }
                        }
                        "Almacenamiento" -> {
                            if (filteredStorages.isEmpty()) EmptyState()
                            else LazyVerticalGrid(
                                columns = GridCells.Adaptive(170.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(filteredStorages) { storage ->
                                    CatalogCard(
                                        image = storage.image?: "",
                                        subtitle = storage.capacity?.toString() ?: "N/D",
                                        name = storage.name,
                                        price = storage.price,
                                        onClick = { onNavigateToComponentDetail("Storage", storage.id)  }
                                    )
                                }
                            }
                        }
                        "PSU" -> {
                            if (filteredPsus.isEmpty()) EmptyState()
                            else LazyVerticalGrid(
                                columns = GridCells.Adaptive(170.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(filteredPsus) { psu ->
                                    CatalogCard(
                                        image = psu.image?: "",
                                        subtitle = psu.efficiency?.uppercase() ?: "N/D",
                                        name = psu.name,
                                        price = psu.price,
                                        onClick = { onNavigateToComponentDetail("PSU", psu.id)  }
                                    )
                                }
                            }
                        }
                        "Gabinetes" -> {
                            if (filteredCases.isEmpty()) EmptyState()
                            else LazyVerticalGrid(
                                columns = GridCells.Adaptive(170.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(filteredCases) { case ->
                                    CatalogCard(
                                        image = case.image?: "",
                                        subtitle = case.type?.uppercase() ?: "N/D",
                                        name = case.name,
                                        price = case.price,
                                        onClick = { onNavigateToComponentDetail("Case", case.id) }
                                    )
                                }
                            }
                        }
                        "Ventiladores CPU" -> {
                            if (filteredCpuCoolers.isEmpty()) EmptyState()
                            else LazyVerticalGrid(
                                columns = GridCells.Adaptive(170.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(filteredCpuCoolers) { cooler ->
                                    CatalogCard(
                                        image = cooler.image?: "",
                                        subtitle = "${cooler.size ?: "?"} MM",
                                        name = cooler.name,
                                        price = cooler.price,
                                        onClick = { onNavigateToComponentDetail("CPUCooler", cooler.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Card genérica, reemplaza CatalogGPUCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CatalogCard(
    image: String,
    subtitle: String,
    name: String,
    price: Float,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard.copy(alpha = 0.5f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color.Black.copy(alpha = 0.2f))
            ) {
                AsyncImage(
                    model = image,
                    contentDescription = name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = subtitle,
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 16.sp
                    )
                }
                Text(
                    text = price.toPrecio(),
                    color = NeonBlue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Estado vacío

@Composable
private fun EmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "No se encontraron componentes", color = OnSurfaceVariant)
    }
}