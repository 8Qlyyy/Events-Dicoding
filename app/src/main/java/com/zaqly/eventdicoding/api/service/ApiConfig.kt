package com.zaqly.eventdicoding.api.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val BASE_URL = "https://event-api.dicoding.dev/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val  eventApiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}