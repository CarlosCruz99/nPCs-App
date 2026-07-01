package com.example.npcsapp.data.auth

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: FirebaseUser?
    fun authStateFlow(): Flow<FirebaseUser?>
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun register(email: String, password: String, name: String): Result<FirebaseUser>
    suspend fun logout()
}
