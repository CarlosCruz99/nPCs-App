package com.example.npcsapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.npcsapp.BuildConfig
import com.example.npcsapp.api.RetrofitInstance
import com.example.npcsapp.data.GPU
import kotlinx.coroutines.launch

class GPUViewModel: ViewModel() {
    var gpuList by mutableStateOf<List<GPU>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)

    init{
        loadShows(BuildConfig.SUPABASE_API_KEY, "Bearer ${BuildConfig.SUPABASE_API_KEY}")
    }

    fun loadShows(apiKey: String, authorization: String){
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitInstance.api.getProducts(apiKey, authorization, 20)
                gpuList = response
            }catch (e: Exception){
                Log.e("GPUViewModel", "No se pudieron cargar las GPUs", e)
            } finally {
                isLoading = false
            }
        }
    }
}