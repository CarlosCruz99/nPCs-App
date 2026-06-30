package com.example.npcsapp.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.npcsapp.api.RetrofitInstance
import com.example.npcsapp.data.GPU
import com.example.npcsapp.data.local.BuildComponentDao
import com.example.npcsapp.data.local.BuildDao
import com.example.npcsapp.data.local.GPUDao
import com.example.npcsapp.data.local.entities.BuildComponentEntity
import com.example.npcsapp.data.local.entities.BuildEntity
import com.example.npcsapp.data.local.entities.GPUEntity
import kotlinx.coroutines.flow.Flow

class ComponentRepository(
    private val gpuDao: GPUDao,
    private val buildDao: BuildDao,
    private val buildComponentDao: BuildComponentDao
) {
    // GPU
    val gpus = gpuDao.getGPUs()

    suspend fun refreshGPUs(
        apiKey: String,
        authorization: String
    ) {
        val response = RetrofitInstance.api.getGPUs(
            apiKey,
            authorization,
            20
        )

            gpuDao.insertListOfGPUs(
                response.map { gpu ->
                    GPUEntity(
                        id = gpu.id,
                        name = gpu.name,
                        price = gpu.price,
                        chipset = gpu.chipset,
                        memory = gpu.memory,
                        coreClock = gpu.coreClock,
                        coreBoost = gpu.coreBoost,
                        color = gpu.color,
                        length = gpu.length,
                        image = gpu.image,
                        amazonLink = gpu.amazonLink
                    )
                }
            )
    }

    suspend fun cacheGPU(gpu: GPU) {
        gpuDao.insertGPU(
            GPUEntity(
                id = gpu.id,
                name = gpu.name,
                price = gpu.price,
                chipset = gpu.chipset,
                memory = gpu.memory,
                coreClock = gpu.coreClock,
                coreBoost = gpu.coreBoost,
                color = gpu.color,
                length = gpu.length,
                image = gpu.image,
                amazonLink = gpu.amazonLink
            )
        )
    }

    // Builds
    val builds = buildDao.getAllBuilds()

    suspend fun createBuild(name: String): Long = buildDao.insertBuild(BuildEntity(name = name))

    fun getBuildById(id: Long): Flow<BuildEntity?> = buildDao.getBuildById(id)

    suspend fun deleteBuild(build: BuildEntity) {
        buildComponentDao.deleteAllComponentsForBuild(build.buildId)
        buildDao.deleteBuild(build)
    }

    fun getComponentsForBuild(buildId: Long) =
        buildDao.searchBuildWithComponentById(buildId)

    suspend fun addGPUToBuild(buildId: Long, gpu: GPU) {
        val existing = buildComponentDao.getComponentForBuild(buildId, "GPU")
        if (existing != null) {
            buildComponentDao.deleteBuildComponent(existing)
        }

        cacheGPU(gpu)

        buildComponentDao.insertBuildComponent(
            BuildComponentEntity(
                buildId = buildId,
                componentType = "GPU",
                componentId = gpu.id,
                displayName = gpu.name
            )
        )
    }

    suspend fun removeComponentFromBuild(component: BuildComponentEntity) {
        buildComponentDao.deleteBuildComponent(component)
    }
}
