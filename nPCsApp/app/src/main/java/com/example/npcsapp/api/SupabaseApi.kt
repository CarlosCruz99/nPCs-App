package com.example.npcsapp.api

import com.example.npcsapp.BuildConfig
import com.example.npcsapp.data.CPU
import com.example.npcsapp.data.CPUCooler
import com.example.npcsapp.data.Case
import com.example.npcsapp.data.GPU
import com.example.npcsapp.data.Motherboard
import com.example.npcsapp.data.PSU
import com.example.npcsapp.data.RAM
import com.example.npcsapp.data.Storage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import kotlin.jvm.java

interface SupabaseApi {
    @GET("rest/v1/video_card")
    suspend fun getGPUs(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int
    ): List<GPU>

    @GET("rest/v1/cpu")
    suspend fun getCPUs(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int
    ): List<CPU>

    @GET("rest/v1/motherboard")
    suspend fun getMotherboards(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int
    ): List<Motherboard>

    @GET("rest/v1/memory")
    suspend fun getRAMs(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int
    ): List<RAM>

    @GET("rest/v1/internal_hard_drive")
    suspend fun getStorages(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int
    ): List<Storage>

    @GET("rest/v1/power_supply")
    suspend fun getPSUs(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int
    ): List<PSU>

    @GET("rest/v1/case")
    suspend fun getCases(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int
    ): List<Case>

    @GET("rest/v1/cpu_cooler")
    suspend fun getCPUCoolers(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int
    ): List<CPUCooler>
}

object RetrofitInstance{
    val api: SupabaseApi by lazy{
        Retrofit.Builder()
            .baseUrl(BuildConfig.SUPABASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupabaseApi::class.java)
    }
}