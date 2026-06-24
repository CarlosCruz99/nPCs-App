package com.example.npcsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.npcsapp.screens.MainScreen
import com.example.npcsapp.ui.theme.NPCsAppTheme
import com.example.npcsapp.viewmodel.BuildViewModel
import com.example.npcsapp.viewmodel.BuildViewModelFactory
import com.example.npcsapp.viewmodel.GPUViewModel
import com.example.npcsapp.viewmodel.GPUViewModelFactory
import kotlin.collections.addAll
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val gpuViewModel: GPUViewModel by viewModels {
        GPUViewModelFactory((application as nPCsApp).repository)
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
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                    MainScreen(
                        gpuViewModel = gpuViewModel,
                        buildViewModel = buildViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    NPCsAppTheme {
//        Greeting("Android")
//    }
//}