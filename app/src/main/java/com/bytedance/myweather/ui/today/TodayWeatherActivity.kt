package com.bytedance.myweather.ui.today

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bytedance.myweather.data.Cast
import com.bytedance.myweather.ui.common.BottomBar
import com.bytedance.myweather.ui.forecast.MainActivity
import com.bytedance.myweather.ui.theme.MyWeatherTheme
import com.bytedance.myweather.viewmodel.WeatherViewModel

class TodayWeatherActivity : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyWeatherTheme {
                TodayWeatherScreen(weatherViewModel) { navigateToForecast() }
            }
        }
    }

    private fun navigateToForecast() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        startActivity(intent)
    }
}

@Composable
fun TodayWeatherScreen(viewModel: WeatherViewModel, onNavigateToForecast: () -> Unit) {
    val weatherData by viewModel.weatherData.collectAsState()
    val currentCity by viewModel.currentCity.collectAsState()
    val todayForecast = weatherData?.forecasts?.firstOrNull()?.casts?.firstOrNull()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF3A82FB), Color(0xFFFC7E51))
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (todayForecast != null) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = weatherData?.forecasts?.first()?.city ?: "",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 40.dp)
                    )
                    Text(
                        text = todayForecast.dayweather,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = "${todayForecast.daytemp}°",
                        color = Color.White,
                        fontSize = 96.sp,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier.padding(top = 30.dp)
                    )
                    Text(
                        text = "最高: ${todayForecast.daytemp}° 最低: ${todayForecast.nighttemp}°",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp
                    )
                    DayNightInfoCard(isDay = true, cast = todayForecast)
                    DayNightInfoCard(isDay = false, cast = todayForecast)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            } else {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(text = "Loading...", color = Color.White, fontSize = 18.sp)
                }
            }
            BottomBar(
                viewModel = viewModel,
                currentCity = currentCity,
                onNavigateToForecast = onNavigateToForecast,
                onNavigateToToday = {},
                isTodayScreen = true
            )
        }
    }
}

@Composable
fun DayNightInfoCard(isDay: Boolean, cast: Cast) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = if (isDay) 40.dp else 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = if (isDay) "☀ 白天" else "🌙 夜间",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "天气\n温度\n风力",
                    color = Color.White.copy(alpha = 0.8f),
                    lineHeight = 24.sp
                )
                Text(
                    text = if (isDay) {
                        "${cast.dayweather}\n${cast.daytemp}°\n${cast.daywind}风 ${cast.daypower}级"
                    } else {
                        "${cast.nightweather}\n${cast.nighttemp}°\n${cast.nightwind}风 ${cast.nightpower}级"
                    },
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp
                )
            }
        }
    }
}