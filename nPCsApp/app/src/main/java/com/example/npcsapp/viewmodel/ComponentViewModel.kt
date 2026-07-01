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

class ComponentViewModel(private val repository: ComponentRepository): ViewModel() {
    val gpuList = repository.gpus
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val cpuList = repository.cpus
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val motherboardList = repository.motherboards
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val ramList = repository.rams
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val storageList = repository.storages
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val psuList = repository.psus
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val caseList = repository.cases
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val cpuCoolerList = repository.cpuCoolers
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    var isLoading by mutableStateOf(false)
        private set

    init {
        refreshAll()
    }

    fun refreshGPUs() {
        viewModelScope.launch {
            isLoading = true

            try {
                repository.refreshGPUs(
                    BuildConfig.SUPABASE_API_KEY,
                    "Bearer ${BuildConfig.SUPABASE_API_KEY}"
                )
            } catch (e: Exception) {
                Log.e("ComponentViewModel", "Error al cargar las GPUs", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun refreshCPUs() {
        viewModelScope.launch {
            isLoading = true
            try {
                repository.refreshCPUs(
                    BuildConfig.SUPABASE_API_KEY,
                    "Bearer ${BuildConfig.SUPABASE_API_KEY}"
                )
            } catch (e: Exception) {
                Log.e("ComponentViewModel", "Error al cargar las CPUs", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun refreshMotherboards() {
        viewModelScope.launch {
            isLoading = true
            try {
                repository.refreshMotherboards(
                    BuildConfig.SUPABASE_API_KEY,
                    "Bearer ${BuildConfig.SUPABASE_API_KEY}"
                )
            } catch (e: Exception) {
                Log.e("ComponentViewModel", "Error al cargar las Motherboards", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun refreshRAMs() {
        viewModelScope.launch {
            isLoading = true
            try {
                repository.refreshRAMs(
                    BuildConfig.SUPABASE_API_KEY,
                    "Bearer ${BuildConfig.SUPABASE_API_KEY}"
                )
            } catch (e: Exception) {
                Log.e("ComponentViewModel", "Error al cargar las RAMs", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun refreshStorages() {
        viewModelScope.launch {
            isLoading = true
            try {
                repository.refreshStorages(
                    BuildConfig.SUPABASE_API_KEY,
                    "Bearer ${BuildConfig.SUPABASE_API_KEY}"
                )
            } catch (e: Exception) {
                Log.e("ComponentViewModel", "Error al cargar los Storages", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun refreshPSUs() {
        viewModelScope.launch {
            isLoading = true
            try {
                repository.refreshPSUs(
                    BuildConfig.SUPABASE_API_KEY,
                    "Bearer ${BuildConfig.SUPABASE_API_KEY}"
                )
            } catch (e: Exception) {
                Log.e("ComponentViewModel", "Error al cargar las PSUs", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun refreshCases() {
        viewModelScope.launch {
            isLoading = true
            try {
                repository.refreshCases(
                    BuildConfig.SUPABASE_API_KEY,
                    "Bearer ${BuildConfig.SUPABASE_API_KEY}"
                )
            } catch (e: Exception) {
                Log.e("ComponentViewModel", "Error al cargar los Cases", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun refreshCPUCoolers() {
        viewModelScope.launch {
            isLoading = true
            try {
                repository.refreshCPUCoolers(
                    BuildConfig.SUPABASE_API_KEY,
                    "Bearer ${BuildConfig.SUPABASE_API_KEY}"
                )
            } catch (e: Exception) {
                Log.e("ComponentViewModel", "Error al cargar los CPU Coolers", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun refreshAll() {
        viewModelScope.launch {
            isLoading = true
            try {
                repository.refreshGPUs(BuildConfig.SUPABASE_API_KEY, "Bearer ${BuildConfig.SUPABASE_API_KEY}")
                repository.refreshCPUs(BuildConfig.SUPABASE_API_KEY, "Bearer ${BuildConfig.SUPABASE_API_KEY}")
                repository.refreshMotherboards(BuildConfig.SUPABASE_API_KEY, "Bearer ${BuildConfig.SUPABASE_API_KEY}")
                repository.refreshRAMs(BuildConfig.SUPABASE_API_KEY, "Bearer ${BuildConfig.SUPABASE_API_KEY}")
                repository.refreshStorages(BuildConfig.SUPABASE_API_KEY, "Bearer ${BuildConfig.SUPABASE_API_KEY}")
                repository.refreshPSUs(BuildConfig.SUPABASE_API_KEY, "Bearer ${BuildConfig.SUPABASE_API_KEY}")
                repository.refreshCases(BuildConfig.SUPABASE_API_KEY, "Bearer ${BuildConfig.SUPABASE_API_KEY}")
                repository.refreshCPUCoolers(BuildConfig.SUPABASE_API_KEY, "Bearer ${BuildConfig.SUPABASE_API_KEY}")
            } catch (e: Exception) {
                Log.e("ComponentViewModel", "Error al cargar los componentes", e)
            } finally {
                isLoading = false
            }
        }
    }



}

class ComponentViewModelFactory(
    private val repository: ComponentRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(ComponentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ComponentViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}