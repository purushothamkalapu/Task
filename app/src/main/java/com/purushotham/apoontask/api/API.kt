package com.purushotham.task.api


import com.purushotham.apoontask.data.ForcastData
import com.purushotham.apoontask.data.TempuratureData
import com.purushotham.apoontask.util.FORECAST
import com.purushotham.apoontask.util.WEATHER_DATA
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface API {
    @GET(FORECAST)
    fun getForcast(
        @Query("appid") apiKey: String,
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String
    ): Call<ForcastData>
    @GET(WEATHER_DATA)
    fun getTemp(
        @Query("appid") apiKey: String,
        @Query("lat") lat: String,
        @Query("lon") lon: String,
    ): Call<TempuratureData>
}