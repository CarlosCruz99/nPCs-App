package com.example.npcsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.npcsapp.data.GPU
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
import com.example.npcsapp.data.repository.ComponentRepository
import com.example.npcsapp.ui.components.toCPU
import com.example.npcsapp.ui.components.toCPUCooler
import com.example.npcsapp.ui.components.toCase
import com.example.npcsapp.ui.components.toGPU
import com.example.npcsapp.ui.components.toMotherboard
import com.example.npcsapp.ui.components.toPSU
import com.example.npcsapp.ui.components.toRAM
import com.example.npcsapp.ui.components.toStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class BuildViewModel(private val repository: ComponentRepository) : ViewModel() {

    val builds = repository.builds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeBuildId = MutableStateFlow<Long?>(null)
    val activeBuildId: StateFlow<Long?> = _activeBuildId

    @OptIn(ExperimentalCoroutinesApi::class)
    val activeBuild: StateFlow<BuildEntity?> = _activeBuildId.flatMapLatest { id ->
        if (id == null) flowOf(null)
        else repository.getBuildById(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val activeBuildComponents: StateFlow<List<BuildComponentEntity>> =
        _activeBuildId.flatMapLatest { buildId ->
            if (buildId != null) {
                repository.getComponentsForBuild(buildId).map { it?.components ?: emptyList() }
            } else {
                flowOf(emptyList())
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList<BuildComponentEntity>())

    fun selectBuild(buildId: Long) {
        _activeBuildId.value = buildId
    }

    fun createBuild(name: String) {
        viewModelScope.launch {
            val newId = repository.createBuild(name)
            _activeBuildId.value = newId
        }
    }

    fun deleteBuild(build: BuildEntity) {
        viewModelScope.launch {
            repository.deleteBuild(build)
            if (_activeBuildId.value == build.buildId) _activeBuildId.value = null
        }
    }

    fun addComponentToBuild(type: String, entity: Any) {
        val buildId = _activeBuildId.value ?: return
        viewModelScope.launch {
            when (type) {
                "GPU" -> repository.addGPUToBuild(buildId, (entity as GPUEntity).toGPU())
                "CPU" -> repository.addCPUToBuild(buildId, (entity as CPUEntity).toCPU())
                "Motherboard" -> repository.addMotherboardToBuild(buildId, (entity as MotherboardEntity).toMotherboard())
                "RAM" -> repository.addRAMToBuild(buildId, (entity as RAMEntity).toRAM())
                "Storage" -> repository.addStorageToBuild(buildId, (entity as StorageEntity).toStorage())
                "PSU" -> repository.addPSUToBuild(buildId, (entity as PSUEntity).toPSU())
                "Case" -> repository.addCaseToBuild(buildId, (entity as CaseEntity).toCase())
                "CPUCooler" -> repository.addCPUCoolerToBuild(buildId, (entity as CPUCoolerEntity).toCPUCooler())
            }
        }
    }

    fun removeComponent(component: BuildComponentEntity) {
        viewModelScope.launch {
            repository.removeComponentFromBuild(component)
        }
    }
}

class BuildViewModelFactory(
    private val repository: ComponentRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BuildViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BuildViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
