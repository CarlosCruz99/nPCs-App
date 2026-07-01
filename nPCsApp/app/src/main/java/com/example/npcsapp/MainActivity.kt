package com.example.npcsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.npcsapp.ui.navigation.nPCsApp
import com.example.npcsapp.ui.theme.NPCsAppTheme
import com.example.npcsapp.viewmodel.BuildViewModel
import com.example.npcsapp.viewmodel.BuildViewModelFactory
import com.example.npcsapp.viewmodel.ComponentViewModel
import com.example.npcsapp.viewmodel.ComponentViewModelFactory
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val componentViewModel: ComponentViewModel by viewModels {
        ComponentViewModelFactory((application as nPCsApp).repository)
    }

    private val buildViewModel: BuildViewModel by viewModels {
        BuildViewModelFactory((application as nPCsApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NPCsAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    nPCsApp(
                        componentViewModel = componentViewModel,
                        buildViewModel = buildViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}