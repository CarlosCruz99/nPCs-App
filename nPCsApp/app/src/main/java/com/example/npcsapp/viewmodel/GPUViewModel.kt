package com.example.npcsapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.npcsapp.BuildConfig
import com.example.npcsapp.data.repository.ComponentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.jvm.java

class GPUViewModel(private val repository: ComponentRepository): ViewModel() {
    val gpuList = repository.gpus
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    var isLoading by mutableStateOf(false)
        private set

    init {
        refreshGPUs()
    }

    private fun refreshGPUs() {
        viewModelScope.launch {
            isLoading = true

            try {
                repository.refreshGPUs(
                    BuildConfig.SUPABASE_API_KEY,
                    "Bearer ${BuildConfig.SUPABASE_API_KEY}"
                )
            } catch (e: Exception) {
                Log.e("GPUViewModel", "Error al cargar las GPUs", e)
            } finally {
                isLoading = false
            }
        }
    }
}

class GPUViewModelFactory(
    private val repository: ComponentRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(GPUViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GPUViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}