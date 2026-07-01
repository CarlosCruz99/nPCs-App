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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.npcsapp.ui.navigation.nPCsApp
import com.example.npcsapp.ui.screens.auth.AuthViewModel
import com.example.npcsapp.ui.theme.NPCsAppTheme
import com.example.npcsapp.viewmodel.BuildViewModel
import com.example.npcsapp.viewmodel.BuildViewModelFactory
import com.example.npcsapp.viewmodel.ChatViewModel
import com.example.npcsapp.viewmodel.ChatViewModelFactory
import com.example.npcsapp.viewmodel.ComponentViewModel
import com.example.npcsapp.viewmodel.ComponentViewModelFactory
import com.example.npcsapp.viewmodel.MarketViewModel
import com.example.npcsapp.viewmodel.MarketViewModelFactory
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val componentViewModel: ComponentViewModel by viewModels {
        ComponentViewModelFactory((application as nPCsApp).repository)
    }

    private val buildViewModel: BuildViewModel by viewModels {
        BuildViewModelFactory((application as nPCsApp).repository)
    }

    private val marketViewModel: MarketViewModel by viewModels {
        MarketViewModelFactory((application as nPCsApp).marketRepository)
    }

    private val chatViewModel: ChatViewModel by viewModels {
        ChatViewModelFactory((application as nPCsApp).chatRepository)
    }

    private val authViewModel: AuthViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                    val authRepository = (application as nPCsApp).authRepository
                    return AuthViewModel(authRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
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
                        authViewModel = authViewModel,
                        marketViewModel = marketViewModel,
                        chatViewModel = chatViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
