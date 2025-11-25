package com.bytedance.myweather.data

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v3/weather/weatherInfo")
    suspend fun getWeather(
        @Query("city") city: String,
        @Query("key") key: String,
        @Query("extensions") extensions: String = "base" // "base"为实况, "all"为预报
    ): WeatherResponse
}
