package com.example.npcsapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.npcsapp.data.GPU
import com.example.npcsapp.data.local.entities.BuildEntity
import com.example.npcsapp.data.local.entities.GPUEntity
import com.example.npcsapp.viewmodel.BuildViewModel
import com.example.npcsapp.viewmodel.GPUViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildsScreen(
    gpuViewModel: GPUViewModel,
    buildViewModel: BuildViewModel,
    onNavigateToDetail: (Long) -> Unit
) {

    val builds by buildViewModel.builds.collectAsState()

    var showNewBuildDialog by remember { mutableStateOf(false) }
    var newBuildName by remember { mutableStateOf("") }

    if (showNewBuildDialog) {
        AlertDialog(
            onDismissRequest = { showNewBuildDialog = false },
            title = { Text("Nueva build") },
            text = {
                OutlinedTextField(
                    value = newBuildName,
                    onValueChange = { newBuildName = it },
                    label = { Text("Nombre de la build") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newBuildName.isNotBlank()) {
                        buildViewModel.createBuild(newBuildName.trim())
                        newBuildName = ""
                        showNewBuildDialog = false
                    }
                }) { Text("Crear") }
            },
            dismissButton = {
                TextButton(onClick = { showNewBuildDialog = false }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Builds")})
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showNewBuildDialog=true }) {
                Icon(Icons.Default.Add, contentDescription = "Crear build")
            }
        }
    ){ padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if(builds.isEmpty()){
                    item{
                        Text("No existen builds", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                else{
                    items(builds) { build ->
                        BuildSection(
                            build = build,
                            onDeleteBuild = {
                                buildViewModel.deleteBuild(it)
                            },
                            onNavigateToDetail = {
                                onNavigateToDetail(it)
                            }
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun BuildSection(
    build: BuildEntity,
    onDeleteBuild: (BuildEntity) -> Unit,
    onNavigateToDetail: (Long) -> Unit
){
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .clickable {
                    onNavigateToDetail(build.buildId)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ){
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = build.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = build.createdAt.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            IconButton(onClick = {onDeleteBuild(build)}) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar build")
            }
        }
    }
}

