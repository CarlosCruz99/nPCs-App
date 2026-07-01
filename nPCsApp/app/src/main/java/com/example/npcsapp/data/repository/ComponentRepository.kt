package com.example.npcsapp.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.npcsapp.api.RetrofitInstance
import com.example.npcsapp.data.CPU
import com.example.npcsapp.data.CPUCooler
import com.example.npcsapp.data.Case
import com.example.npcsapp.data.GPU
import com.example.npcsapp.data.Motherboard
import com.example.npcsapp.data.PSU
import com.example.npcsapp.data.RAM
import com.example.npcsapp.data.Storage
import com.example.npcsapp.data.local.BuildComponentDao
import com.example.npcsapp.data.local.BuildDao
import com.example.npcsapp.data.local.CPUCoolerDao
import com.example.npcsapp.data.local.CPUDao
import com.example.npcsapp.data.local.CaseDao
import com.example.npcsapp.data.local.GPUDao
import com.example.npcsapp.data.local.MotherboardDao
import com.example.npcsapp.data.local.PSUDao
import com.example.npcsapp.data.local.RAMDao
import com.example.npcsapp.data.local.StorageDao
import com.example.npcsapp.data.local.entities.BuildComponentEntity
import com.example.npcsapp.data.local.entities.BuildEntity
import com.example.npcsapp.data.local.entities.CPUCoolerEntity
import com.example.npcsapp.data.local.entities.CPUEntity
import com.example.npcsapp.data.local.entities.CaseEntity
import com.example.npcsapp.data.local.entities.GPUEntity
import com.example.npcsapp.data.local.entities.MotherboardEntity
import com.example.npcsapp.data.local.entities.PSUEntity
import com.example.npcsapp.data.local.entities.RAMEntity
import com.example.npcsapp.data.local.entities.StorageEntity
import kotlinx.coroutines.flow.Flow

class ComponentRepository(
    private val gpuDao: GPUDao,
    private val cpuDao: CPUDao,
    private val motherboardDao: MotherboardDao,
    private val ramDao: RAMDao,
    private val storageDao: StorageDao,
    private val psuDao: PSUDao,
    private val caseDao: CaseDao,
    private val cpuCoolerDao: CPUCoolerDao,
    private val buildDao: BuildDao,
    private val buildComponentDao: BuildComponentDao
) {
    // GPU
    val gpus = gpuDao.getGPUs()
    val cpus = cpuDao.getCPUs()
    val motherboards = motherboardDao.getMotherboards()
    val rams = ramDao.getRAMs()
    val storages = storageDao.getStorages()
    val psus = psuDao.getPSUs()
    val cases = caseDao.getCases()
    val cpuCoolers = cpuCoolerDao.getCPUCoolers()

    // GPU
    suspend fun refreshGPUs(
        apiKey: String,
        authorization: String
    ) {
        val response = RetrofitInstance.api.getGPUs(
            apiKey,
            authorization,
            200
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

    // CPU
    suspend fun refreshCPUs(
        apiKey: String,
        authorization: String
    ) {
        val response = RetrofitInstance.api.getCPUs(
            apiKey,
            authorization,
            200
        )

        cpuDao.insertListOfCPUs(
            response.map { cpu ->
                CPUEntity(
                    id = cpu.id,
                    name = cpu.name,
                    price = cpu.price,
                    coreCount = cpu.coreCount,
                    coreClock = cpu.coreClock,
                    boostClock = cpu.boostClock,
                    microarchitecture = cpu.microarchitecture,
                    tdp = cpu.tdp,
                    graphics = cpu.graphics,
                    image = cpu.image,
                    amazonLink = cpu.amazonLink

                )
            }
        )
    }

    suspend fun cacheCPU(cpu: CPU) {
        cpuDao.insertCPU(
            CPUEntity(
                id = cpu.id,
                name = cpu.name,
                price = cpu.price,
                coreCount = cpu.coreCount,
                coreClock = cpu.coreClock,
                boostClock = cpu.boostClock,
                microarchitecture = cpu.microarchitecture,
                tdp = cpu.tdp,
                graphics = cpu.graphics,
                image = cpu.image,
                amazonLink = cpu.amazonLink

            )
        )
    }

    //Motherboard
    suspend fun refreshMotherboards(
        apiKey: String,
        authorization: String
    ) {
        val response = RetrofitInstance.api.getMotherboards(
            apiKey,
            authorization,
            200
        )
        motherboardDao.insertListOfMotherboards(
            response.map { motherboard ->
                MotherboardEntity(
                    id = motherboard.id,
                    name = motherboard.name,
                    price = motherboard.price,
                    socket = motherboard.socket,
                    formFactor = motherboard.formFactor,
                    maxMemory = motherboard.maxMemory,
                    memorySlots = motherboard.memorySlots,
                    color = motherboard.color,
                    image = motherboard.image,
                    amazonLink = motherboard.amazonLink
                )
            }
        )
    }

    suspend fun cacheMotherboard(motherboard: Motherboard) {
        motherboardDao.insertMotherboard(
            MotherboardEntity(
                id = motherboard.id,
                name = motherboard.name,
                price = motherboard.price,
                socket = motherboard.socket,
                formFactor = motherboard.formFactor,
                maxMemory = motherboard.maxMemory,
                memorySlots = motherboard.memorySlots,
                color = motherboard.color,
                image = motherboard.image,
                amazonLink = motherboard.amazonLink
            )
        )
    }

    //RAM
    suspend fun refreshRAMs(
        apiKey: String,
        authorization: String
    ) {
        val response = RetrofitInstance.api.getRAMs(
            apiKey,
            authorization,
            200
        )
        ramDao.insertListOfRAMs(
            response.map { ram ->
                RAMEntity(
                    id = ram.id,
                    name = ram.name,
                    price = ram.price,
                    generation = ram.generation,
                    speed = ram.speed,
                    modules = ram.modules,
                    moduleMemory = ram.moduleMemory,
                    pricePerGB = ram.pricePerGB,
                    color = ram.color,
                    firstWordLatency = ram.firstWordLatency,
                    casLatency = ram.casLatency,
                    image = ram.image,
                    amazonLink = ram.amazonLink
                )
            }
        )
    }

    suspend fun cacheRAM(ram: RAM) {
        ramDao.insertRAM(
            RAMEntity(
                id = ram.id,
                name = ram.name,
                price = ram.price,
                generation = ram.generation,
                speed = ram.speed,
                modules = ram.modules,
                moduleMemory = ram.moduleMemory,
                pricePerGB = ram.pricePerGB,
                color = ram.color,
                firstWordLatency = ram.firstWordLatency,
                casLatency = ram.casLatency,
                image = ram.image,
                amazonLink = ram.amazonLink
            )
        )
    }

    //Storage
    suspend fun refreshStorages(
        apiKey: String,
        authorization: String
    ) {
        val response = RetrofitInstance.api.getStorages(
            apiKey,
            authorization,
            200
        )
        storageDao.insertListOfStorages(
            response.map { storage ->
                StorageEntity(
                    id = storage.id,
                    name = storage.name,
                    price = storage.price,
                    capacity = storage.capacity,
                    pricePerGB = storage.pricePerGB,
                    type = storage.type,
                    cache = storage.cache,
                    formFactor = storage.formFactor,
                    `interface` = storage.`interface`,
                    image = storage.image,
                    amazonLink = storage.amazonLink
                )
            }
        )
    }

    suspend fun cacheStorage(storage: Storage) {
        storageDao.insertStorage(
            StorageEntity(
                id = storage.id,
                name = storage.name,
                price = storage.price,
                capacity = storage.capacity,
                pricePerGB = storage.pricePerGB,
                type = storage.type,
                cache = storage.cache,
                formFactor = storage.formFactor,
                `interface` = storage.`interface`,
                image = storage.image,
                amazonLink = storage.amazonLink
            )
        )
    }

    //PSU
    suspend fun refreshPSUs(
        apiKey: String,
        authorization: String
    ) {
        val response = RetrofitInstance.api.getPSUs(
            apiKey,
            authorization,
            200
        )
        psuDao.insertListOfPSUs(
            response.map { psu ->
                PSUEntity(
                    id = psu.id,
                    name = psu.name,
                    price = psu.price,
                    type = psu.type,
                    efficiency = psu.efficiency,
                    wattage = psu.wattage,
                    modular = psu.modular,
                    color = psu.color,
                    image = psu.image,
                    amazonLink = psu.amazonLink
                )
            }
        )
    }

    suspend fun cachePSU(psu: PSU) {
        psuDao.insertPSU(
            PSUEntity(
                id = psu.id,
                name = psu.name,
                price = psu.price,
                type = psu.type,
                efficiency = psu.efficiency,
                wattage = psu.wattage,
                modular = psu.modular,
                color = psu.color,
                image = psu.image,
                amazonLink = psu.amazonLink
            )
        )
    }

    //Case
    suspend fun refreshCases(
        apiKey: String,
        authorization: String
    ) {
        val response = RetrofitInstance.api.getCases(
            apiKey,
            authorization,
            200
        )
        caseDao.insertListOfCases(
            response.map { case ->
                CaseEntity(
                    id = case.id,
                    name = case.name,
                    price = case.price,
                    type = case.type,
                    color = case.color,
                    psu = case.psu,
                    sidePanel = case.sidePanel,
                    externalVolume = case.externalVolume,
                    interna35Bays = case.interna35Bays,
                    image = case.image,
                    amazonLink = case.amazonLink
                )
            }
        )
    }

    suspend fun cacheCases(pcCase: Case) {
        caseDao.insertCase(
            CaseEntity(
                id = pcCase.id,
                name = pcCase.name,
                price = pcCase.price,
                type = pcCase.type,
                color = pcCase.color,
                psu = pcCase.psu,
                sidePanel = pcCase.sidePanel,
                externalVolume = pcCase.externalVolume,
                interna35Bays = pcCase.interna35Bays,
                image = pcCase.image,
                amazonLink = pcCase.amazonLink
            )
        )
    }

    //CPU Cooler
    suspend fun refreshCPUCoolers(
        apiKey: String,
        authorization: String
    ) {
        val response = RetrofitInstance.api.getCPUCoolers(
            apiKey,
            authorization,
            200
        )
        cpuCoolerDao.insertListOfCPUCoolers(
            response.map { cpuCooler ->
                CPUCoolerEntity(
                    id = cpuCooler.id,
                    name = cpuCooler.name,
                    price = cpuCooler.price,
                    minRPM = cpuCooler.minRPM,
                    minNoiseLevel = cpuCooler.minNoiseLevel,
                    color = cpuCooler.color,
                    size = cpuCooler.size,
                    image = cpuCooler.image,
                    amazonLink = cpuCooler.amazonLink,
                    maxRPM = cpuCooler.maxRPM,
                    maxNoiseLevel = cpuCooler.maxNoiseLevel
                )
            }
        )
    }

    suspend fun cacheCPUCoolers(cpuCooler: CPUCooler) {
        cpuCoolerDao.insertCPUCooler(
            CPUCoolerEntity(
                id = cpuCooler.id,
                name = cpuCooler.name,
                price = cpuCooler.price,
                minRPM = cpuCooler.minRPM,
                minNoiseLevel = cpuCooler.minNoiseLevel,
                color = cpuCooler.color,
                size = cpuCooler.size,
                image = cpuCooler.image,
                amazonLink = cpuCooler.amazonLink,
                maxRPM = cpuCooler.maxRPM,
                maxNoiseLevel = cpuCooler.maxNoiseLevel
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

    suspend fun addCPUToBuild(buildId: Long, cpu: CPU) {
        val existing = buildComponentDao.getComponentForBuild(buildId, "CPU")
        if (existing != null) {
            buildComponentDao.deleteBuildComponent(existing)
        }

        cacheCPU(cpu)

        buildComponentDao.insertBuildComponent(
            BuildComponentEntity(
                buildId = buildId,
                componentType = "CPU",
                componentId = cpu.id,
                displayName = cpu.name
            )
        )
    }

    suspend fun addMotherboardToBuild(buildId: Long, motherboard: Motherboard) {
        val existing = buildComponentDao.getComponentForBuild(buildId, "Motherboard")
        if (existing != null) {
            buildComponentDao.deleteBuildComponent(existing)
        }

        cacheMotherboard(motherboard)

        buildComponentDao.insertBuildComponent(
            BuildComponentEntity(
                buildId = buildId,
                componentType = "Motherboard",
                componentId = motherboard.id,
                displayName = motherboard.name
            )
        )
    }

    suspend fun addRAMToBuild(buildId: Long, ram: RAM) {
        val existing = buildComponentDao.getComponentForBuild(buildId, "RAM")
        if (existing != null) {
            buildComponentDao.deleteBuildComponent(existing)
        }

        cacheRAM(ram)

        buildComponentDao.insertBuildComponent(
            BuildComponentEntity(
                buildId = buildId,
                componentType = "RAM",
                componentId = ram.id,
                displayName = ram.name
            )
        )
    }

    suspend fun addStorageToBuild(buildId: Long, storage: Storage) {
        val existing = buildComponentDao.getComponentForBuild(buildId, "Storage")
        if (existing != null) {
            buildComponentDao.deleteBuildComponent(existing)
        }

        cacheStorage(storage)

        buildComponentDao.insertBuildComponent(
            BuildComponentEntity(
                buildId = buildId,
                componentType = "Storage",
                componentId = storage.id,
                displayName = storage.name
            )
        )
    }

    suspend fun addPSUToBuild(buildId: Long, psu: PSU) {
        val existing = buildComponentDao.getComponentForBuild(buildId, "PSU")
        if (existing != null) {
            buildComponentDao.deleteBuildComponent(existing)
        }

        cachePSU(psu)

        buildComponentDao.insertBuildComponent(
            BuildComponentEntity(
                buildId = buildId,
                componentType = "PSU",
                componentId = psu.id,
                displayName = psu.name
            )
        )
    }

    suspend fun addCaseToBuild(buildId: Long, case: Case) {
        val existing = buildComponentDao.getComponentForBuild(buildId, "Case")
        if (existing != null) {
            buildComponentDao.deleteBuildComponent(existing)
        }

        cacheCases(case)

        buildComponentDao.insertBuildComponent(
            BuildComponentEntity(
                buildId = buildId,
                componentType = "Case",
                componentId = case.id,
                displayName = case.name
            )
        )
    }

    suspend fun addCPUCoolerToBuild(buildId: Long, cpuCooler: CPUCooler) {
        val existing = buildComponentDao.getComponentForBuild(buildId, "CPUCooler")
        if (existing != null) {
            buildComponentDao.deleteBuildComponent(existing)
        }

        cacheCPUCoolers(cpuCooler)

        buildComponentDao.insertBuildComponent(
            BuildComponentEntity(
                buildId = buildId,
                componentType = "CPUCooler",
                componentId = cpuCooler.id,
                displayName = cpuCooler.name
            )
        )
    }

    suspend fun removeComponentFromBuild(component: BuildComponentEntity) {
        buildComponentDao.deleteBuildComponent(component)
    }
}
