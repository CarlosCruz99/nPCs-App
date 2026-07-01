package com.example.npcsapp.data.auth

import com.example.npcsapp.data.local.UserDao
import com.example.npcsapp.data.local.entities.user.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val userDao: UserDao,
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : AuthRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override fun authStateFlow(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }

    override suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                // Sincronizar con base de datos local
                val userEntity = UserEntity(
                    userId = user.uid,
                    name = user.displayName ?: "Usuario",
                    email = user.email ?: email,
                    profileImageUrl = user.photoUrl?.toString()
                )
                userDao.insertUser(userEntity)
                
                // Sincronizar con Firestore para que otros usuarios puedan encontrarlo
                saveUserToFirestore(userEntity)
                
                Result.success(user)
            } else {
                Result.failure(Exception("Login failed: User is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String, name: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                user.updateProfile(profileUpdates).await()
                
                val userEntity = UserEntity(
                    userId = user.uid,
                    name = name,
                    email = email,
                    profileImageUrl = null
                )
                
                // Persistencia local
                userDao.insertUser(userEntity)
                
                // Persistencia en Firestore
                saveUserToFirestore(userEntity)

                Result.success(user)
            } else {
                Result.failure(Exception("Registration failed: User is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveUserToFirestore(user: UserEntity) {
        val userData = hashMapOf(
            "userId" to user.userId,
            "name" to user.name,
            "email" to user.email,
            "profileImageUrl" to user.profileImageUrl
        )
        usersCollection.document(user.userId).set(userData).await()
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }
}
