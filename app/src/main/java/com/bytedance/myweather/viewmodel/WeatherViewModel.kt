package com.bytedance.myweather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytedance.myweather.data.NetworkManager
import com.bytedance.myweather.data.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData

    private val _currentCity = MutableStateFlow("广州")
    val currentCity: StateFlow<String> = _currentCity

    private val cityCodes = mapOf(
        "北京" to "110101",
        "上海" to "310000",
        "广州" to "440100",
        "深圳" to "440300"
    )

    init {
        fetchWeather()
    }

    fun fetchWeather() {
        val cityCode = cityCodes[currentCity.value] ?: "110101"
        viewModelScope.launch {
            try {
                val response = NetworkManager.api.getWeather(
                    city = cityCode,
                    key = "79cb9d323f03829f54d7a187e2d66d7a",
                    extensions = "all"
                )
                if (response.status == "1") {
                    _weatherData.value = response
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun changeCity(cityName: String) {
        _currentCity.value = cityName
        fetchWeather()
    }
}