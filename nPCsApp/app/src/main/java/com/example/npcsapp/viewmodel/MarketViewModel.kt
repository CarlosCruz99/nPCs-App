package com.example.npcsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.npcsapp.data.local.entities.MarketItemEntity
import com.example.npcsapp.data.repository.MarketRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MarketViewModel(private val repository: MarketRepository) : ViewModel() {

    val marketItems: StateFlow<List<MarketItemEntity>> = repository.allMarketItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun publishItem(
        title: String,
        price: Float,
        description: String,
        location: String,
        sellerName: String,
        sellerId: String,
        condition: String
    ) {
        viewModelScope.launch {
            val newItem = MarketItemEntity(
                title = title,
                price = price,
                description = description,
                location = location,
                sellerName = sellerName,
                sellerId = sellerId,
                condition = condition
            )
            repository.insert(newItem)
        }
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }
}

class MarketViewModelFactory(private val repository: MarketRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarketViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MarketViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
