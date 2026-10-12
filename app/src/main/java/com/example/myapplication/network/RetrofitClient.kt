package com.example.myapplication.network

import com.example.myapplication.model.Viaje
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/solicitudes")
    suspend fun enviarSolicitud(@Body viaje: Viaje): Viaje
}

object RetrofitClient {
    private const val BASE_URL = "https://tu-backend-springboot.com"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}