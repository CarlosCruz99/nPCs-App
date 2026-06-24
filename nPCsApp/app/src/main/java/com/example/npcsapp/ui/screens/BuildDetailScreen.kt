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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.npcsapp.data.local.entities.BuildEntity
import com.example.npcsapp.viewmodel.BuildViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildDetailScreen(
    buildId: Long,
    buildViewModel: BuildViewModel,
    onNavigateToComponentDetail: (Long) -> Unit,
    onBack: () -> Unit
){
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
//        floatingActionButton = {
//            FloatingActionButton(onClick = { onBack() }) {
//                Icon(Icons.Default.ArrowBack, contentDescription = "Volver a listado de builds")
//            }
//        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item{
                PcComponentSection("GPU", 1, onNavigateToComponentDetail = {onNavigateToComponentDetail(it)})
            }

        }
    }
}

@Composable
fun PcComponentSection(
    componentType: String,
    componentId: Long,
    onNavigateToComponentDetail: (Long) -> Unit
){
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .clickable {
                    onNavigateToComponentDetail(componentId)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ){
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar componente"
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = componentType,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}