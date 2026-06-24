package com.example.npcsapp.api

import com.example.npcsapp.BuildConfig
import com.example.npcsapp.data.GPU
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