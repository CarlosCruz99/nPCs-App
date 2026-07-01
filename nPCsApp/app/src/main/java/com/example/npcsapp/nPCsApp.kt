package com.example.npcsapp

import android.app.Application
import com.example.npcsapp.data.auth.AuthRepository
import com.example.npcsapp.data.auth.FirebaseAuthRepository
import com.example.npcsapp.data.local.AppDatabase
import com.example.npcsapp.data.repository.ChatRepository
import com.example.npcsapp.data.repository.ComponentRepository
import com.example.npcsapp.data.repository.MarketRepository

class nPCsApp: Application() {
    val database by lazy { AppDatabase.getDatabase(this) }

    val repository by lazy {
        ComponentRepository(
            gpuDao = database.GPUDao(),
            buildDao = database.BuildDao(),
            cpuDao = database.CPUDao(),
            motherboardDao = database.MotherboardDao(),
            ramDao = database.RAMDao(),
            storageDao = database.StorageDao(),
            psuDao = database.PSUDao(),
            caseDao = database.CaseDao(),
            cpuCoolerDao = database.CPUCoolerDao(),
            buildComponentDao = database.BuildComponentDao()
        )
    }

    val marketRepository by lazy {
        MarketRepository(database.MarketItemDao())
    }

    val chatRepository by lazy {
        ChatRepository(database.ChatDao(), database.UserDao())
    }

    val authRepository: AuthRepository by lazy {
        FirebaseAuthRepository(userDao = database.UserDao())
    }
}
