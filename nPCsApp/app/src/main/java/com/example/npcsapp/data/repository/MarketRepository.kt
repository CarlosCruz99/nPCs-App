package com.example.npcsapp.data.repository

import com.example.npcsapp.data.local.MarketItemDao
import com.example.npcsapp.data.local.entities.MarketItemEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MarketRepository(private val marketItemDao: MarketItemDao) {
    private val firestore = FirebaseFirestore.getInstance()
    private val marketCollection = firestore.collection("market_items")

    /**
     * Obtiene todos los artículos desde Firestore en tiempo real.
     * Mapea el ID de documento (String) a un Long (hashCode) para compatibilidad con Room.
     */
    val allMarketItems: Flow<List<MarketItemEntity>> = callbackFlow {
        val subscription = marketCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val items = snapshot.documents.mapNotNull { doc ->
                        val item = doc.toObject(MarketItemEntity::class.java)
                        // IMPORTANTE: El ID de Room es Long, pero Firestore usa String.
                        // Usamos el hashCode del ID de Firebase para mantener la estructura actual.
                        item?.copy(id = doc.id.hashCode().toLong())
                    }
                    trySend(items)
                }
            }
        awaitClose { subscription.remove() }
    }

    /**
     * Inserta un nuevo artículo en Firestore.
     */
    suspend fun insert(item: MarketItemEntity) {
        val itemMap = hashMapOf(
            "title" to item.title,
            "price" to item.price,
            "description" to item.description,
            "location" to item.location,
            "sellerName" to item.sellerName,
            "sellerId" to item.sellerId,
            "condition" to item.condition,
            "imageUrl" to item.imageUrl,
            "createdAt" to item.createdAt
        )
        // Subimos a la nube
        marketCollection.add(itemMap).await()
    }

    suspend fun delete(id: Long) {
        // La eliminación global requeriría el ID original de Firestore.
        // Por ahora, eliminamos localmente.
        marketItemDao.deleteMarketItem(id)
    }

    suspend fun getItemById(id: Long): MarketItemEntity? {
        return marketItemDao.getMarketItemById(id)
    }
}
