package com.example.npcsapp

import android.app.Application
import com.example.npcsapp.data.local.AppDatabase
import com.example.npcsapp.data.repository.ComponentRepository

class nPCsApp: Application() {
    val database by lazy { AppDatabase.getDatabase(this) }

    val repository by lazy {
        ComponentRepository(
            gpuDao = database.GPUDao(),
            buildDao = database.BuildDao(),
            buildComponentDao = database.BuildComponentDao()
        )
    }
}