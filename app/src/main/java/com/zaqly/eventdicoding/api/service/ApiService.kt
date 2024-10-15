package com.zaqly.eventdicoding.api.service


import com.zaqly.eventdicoding.api.model.DetailEventResponse
import com.zaqly.eventdicoding.api.model.EventModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/events")
    fun getUpcomingEvents(@Query("active") active: Int = 1): Call<EventModel>

    @GET("/events")
    fun getFinishedEvents(@Query("active") active: Int = 0): Call<EventModel>

    @GET("events/{id}")
    fun getEventDetails(@Path("id") id: String): Call<DetailEventResponse>
}