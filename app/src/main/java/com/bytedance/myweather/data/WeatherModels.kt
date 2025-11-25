package com.bytedance.myweather.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val status: String,
    val info: String,
    val lives: List<Live>?,
    val forecasts: List<Forecast>?
)

data class Live(
    val province: String,
    val city: String,
    val weather: String,
    val temperature: String,
    @SerializedName("winddirection") val windDirection: String,
    @SerializedName("windpower") val windPower: String,
    @SerializedName("reporttime") val reportTime: String
)

data class Forecast(
    val city: String,
    val casts: List<Cast>
)

data class Cast(
    val date: String,
    val dayweather: String,
    val nightweather: String,
    val daytemp: String,
    val nighttemp: String,
    val daywind: String,
    val nightwind: String,
    val daypower: String,
    val nightpower: String
)
