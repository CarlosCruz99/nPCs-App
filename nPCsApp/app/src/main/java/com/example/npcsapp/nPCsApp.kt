package com.example.npcsapp

import android.app.Application
import com.example.npcsapp.data.auth.AuthRepository
import com.example.npcsapp.data.auth.FirebaseAuthRepository
import com.example.npcsapp.data.local.AppDatabase
import com.example.npcsapp.data.repository.ComponentRepository

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

    val authRepository: AuthRepository by lazy {
        FirebaseAuthRepository(userDao = database.UserDao())
    }
}
