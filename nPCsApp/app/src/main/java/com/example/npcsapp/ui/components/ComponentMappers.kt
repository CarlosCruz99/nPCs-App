package com.example.npcsapp.ui.components

import com.example.npcsapp.data.*
import com.example.npcsapp.data.local.entities.*

data class ComponentDisplayInfo(
    val id: Int,
    val name: String,
    val price: Float,
    val image: String?,
    val amazonLink: String?
)

// Entity -> DisplayInfo
fun displayInfoOf(type: String, entity: Any): ComponentDisplayInfo = when (type) {
    "GPU" -> (entity as GPUEntity).let { ComponentDisplayInfo(it.id, it.name, it.price, it.image, it.amazonLink) }
    "CPU" -> (entity as CPUEntity).let { ComponentDisplayInfo(it.id, it.name, it.price, it.image, it.amazonLink) }
    "Motherboard" -> (entity as MotherboardEntity).let { ComponentDisplayInfo(it.id, it.name, it.price, it.image, it.amazonLink) }
    "RAM" -> (entity as RAMEntity).let { ComponentDisplayInfo(it.id, it.name, it.price, it.image, it.amazonLink) }
    "Storage" -> (entity as StorageEntity).let { ComponentDisplayInfo(it.id, it.name, it.price, it.image, it.amazonLink) }
    "PSU" -> (entity as PSUEntity).let { ComponentDisplayInfo(it.id, it.name, it.price, it.image, it.amazonLink) }
    "Case" -> (entity as CaseEntity).let { ComponentDisplayInfo(it.id, it.name, it.price, it.image, it.amazonLink) }
    "CPUCooler" -> (entity as CPUCoolerEntity).let { ComponentDisplayInfo(it.id, it.name, it.price, it.image, it.amazonLink) }
    else -> ComponentDisplayInfo(0, "", 0f, "", null)
}

private const val NA = "N/D"

// Short subtitle shown on cards/badges
fun subtitleFor(type: String, entity: Any): String = when (type) {
    "GPU" -> (entity as GPUEntity).chipset ?: NA
    "CPU" -> (entity as CPUEntity).microarchitecture ?: NA
    "Motherboard" -> (entity as MotherboardEntity).socket ?: NA
    "RAM" -> (entity as RAMEntity).let { "${it.moduleMemory ?: "?"} GB x${it.modules ?: "?"}" }
    "Storage" -> (entity as StorageEntity).let { "${it.capacity ?: "?"} GB" }
    "PSU" -> (entity as PSUEntity).efficiency ?: NA
    "Case" -> (entity as CaseEntity).type ?: NA
    "CPUCooler" -> (entity as CPUCoolerEntity).let { "${it.size ?: "?"} mm" }
    else -> NA
}

fun specsFor(type: String, entity: Any): List<Pair<String, String>> = when (type) {
    "GPU" -> (entity as GPUEntity).let {
        listOf(
            "Chipset" to (it.chipset ?: NA),
            "Memoria" to (it.memory?.let { m -> "${m.toInt()} GB" } ?: NA),
            "Frecuencia base" to (it.coreClock?.let { c -> "$c MHz" } ?: NA),
            "Frecuencia boost" to (it.coreBoost?.let { c -> "$c MHz" } ?: NA),
            "Color" to (it.color ?: NA),
            "Longitud" to (it.length?.let { l -> "$l mm" } ?: NA)
        )
    }
    "CPU" -> (entity as CPUEntity).let {
        listOf(
            "Núcleos" to (it.coreCount?.toString() ?: NA),
            "Frecuencia base" to (it.coreClock?.let { c -> "$c GHz" } ?: NA),
            "Frecuencia boost" to (it.boostClock?.let { c -> "$c GHz" } ?: NA),
            "Microarquitectura" to (it.microarchitecture ?: NA),
            "TDP" to (it.tdp?.let { t -> "$t W" } ?: NA),
            "Gráficos integrados" to (it.graphics ?: NA)
        )
    }
    "Motherboard" -> (entity as MotherboardEntity).let {
        listOf(
            "Socket" to (it.socket ?: NA),
            "Form Factor" to (it.formFactor ?: NA),
            "Memoria máxima" to (it.maxMemory?.let { m -> "${m.toInt()} GB" } ?: NA),
            "Slots de memoria" to (it.memorySlots?.toString() ?: NA),
            "Color" to (it.color ?: NA)
        )
    }
    "RAM" -> (entity as RAMEntity).let {
        listOf(
            "Generación" to (it.generation?.let { g -> "DDR$g" } ?: NA),
            "Velocidad" to (it.speed?.let { s -> "$s MHz" } ?: NA),
            "Módulos" to (it.modules?.toString() ?: NA),
            "Memoria por módulo" to (it.moduleMemory?.let { m -> "$m GB" } ?: NA),
            "Precio por GB" to (it.pricePerGB?.let { p -> "$$p" } ?: NA),
            "Color" to (it.color ?: NA),
            "CAS Latency" to (it.casLatency?.toString() ?: NA)
        )
    }
    "Storage" -> (entity as StorageEntity).let {
        listOf(
            "Capacidad" to (it.capacity?.let { c -> "$c GB" } ?: NA),
            "Tipo" to (it.type ?: NA),
            "Caché" to (it.cache?.let { c -> "$c MB" } ?: NA),
            "Form Factor" to (it.formFactor ?: NA),
            "Interfaz" to (it.`interface` ?: NA),
            "Precio por GB" to (it.pricePerGB?.let { p -> "$$p" } ?: NA)
        )
    }
    "PSU" -> (entity as PSUEntity).let {
        listOf(
            "Tipo" to (it.type ?: NA),
            "Eficiencia" to (it.efficiency ?: NA),
            "Wattage" to (it.wattage?.let { w -> "$w W" } ?: NA),
            "Modular" to (it.modular ?: NA),
            "Color" to (it.color ?: NA)
        )
    }
    "Case" -> (entity as CaseEntity).let {
        listOf(
            "Tipo" to (it.type ?: NA),
            "Color" to (it.color ?: NA),
            "PSU incluida" to (it.psu?.let { p -> if (p > 0) "Sí" else "No" } ?: NA),
            "Panel lateral" to (it.sidePanel ?: NA),
            "Volumen externo" to (it.externalVolume?.let { v -> "$v L" } ?: NA),
            "Bahías 3.5\"" to (it.interna35Bays?.toString() ?: NA)
        )
    }
    "CPUCooler" -> (entity as CPUCoolerEntity).let {
        listOf(
            "RPM mínimo" to (it.minRPM?.toString() ?: NA),
            "RPM máximo" to (it.maxRPM?.toString() ?: NA),
            "Ruido mínimo" to (it.minNoiseLevel?.let { n -> "$n dB" } ?: NA),
            "Ruido máximo" to (it.maxNoiseLevel?.let { n -> "$n dB" } ?: NA),
            "Tamaño" to (it.size?.let { s -> "$s mm" } ?: NA),
            "Color" to (it.color ?: NA)
        )
    }
    else -> emptyList()
}

// Search matching, per type
fun matchesQuery(type: String, entity: Any, query: String): Boolean {
    if (query.isBlank()) return true
    return when (type) {
        "GPU" -> (entity as GPUEntity).let { it.name.contains(query, true) || (it.chipset?.contains(query, true) ?: false) }
        "CPU" -> (entity as CPUEntity).let { it.name.contains(query, true) || (it.microarchitecture?.contains(query, true) ?: false) }
        "Motherboard" -> (entity as MotherboardEntity).let { it.name.contains(query, true) || (it.socket?.contains(query, true) ?: false) }
        "RAM" -> (entity as RAMEntity).name.contains(query, true)
        "Storage" -> (entity as StorageEntity).name.contains(query, true)
        "PSU" -> (entity as PSUEntity).let { it.name.contains(query, true) || (it.efficiency?.contains(query, true) ?: false) }
        "Case" -> (entity as CaseEntity).let { it.name.contains(query, true) || (it.type?.contains(query, true) ?: false) }
        "CPUCooler" -> (entity as CPUCoolerEntity).name.contains(query, true)
        else -> false
    }
}

// Entity -> data-class
fun GPUEntity.toGPU() = GPU(id, name, price, chipset, memory, coreClock, coreBoost, color, length, image, amazonLink)
fun CPUEntity.toCPU() = CPU(id, name, price, coreCount, coreClock, boostClock, microarchitecture, tdp, graphics, image, amazonLink)
fun MotherboardEntity.toMotherboard() = Motherboard(id, name, price, socket, formFactor, maxMemory, memorySlots, color, image, amazonLink)
fun RAMEntity.toRAM() = RAM(id, name, price, generation, speed, modules, moduleMemory, pricePerGB, color, firstWordLatency, casLatency, image, amazonLink)
fun StorageEntity.toStorage() = Storage(id, name, price, capacity, pricePerGB, type, cache, formFactor, `interface`, image, amazonLink)
fun PSUEntity.toPSU() = PSU(id, name, price, type, efficiency, wattage, modular, color, image, amazonLink)
fun CaseEntity.toCase() = Case(id, name, price, type, color, psu, sidePanel, externalVolume, interna35Bays, image, amazonLink)
fun CPUCoolerEntity.toCPUCooler() = CPUCooler(id, name, price, minRPM, minNoiseLevel, color, size, image, amazonLink, maxRPM, maxNoiseLevel)

// The ordered list of slots shown in BuildDetailScreen
val componentSlots = listOf(
    "GPU" to "Selecciona tu Tarjeta Gráfica",
    "CPU" to "Selecciona un procesador",
    "Motherboard" to "Placa base compatible",
    "RAM" to "Elige velocidad y capacidad",
    "Storage" to "Almacenamiento NVMe/SSD",
    "PSU" to "Fuente de poder confiable",
    "Case" to "Elige tu gabinete",
    "CPUCooler" to "Enfriamiento para tu CPU"
)