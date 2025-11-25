package com.bytedance.myweather.ui.forecast

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bytedance.myweather.data.Cast
import com.bytedance.myweather.ui.common.BottomBar
import com.bytedance.myweather.ui.today.TodayWeatherActivity
import com.bytedance.myweather.ui.theme.MyWeatherTheme
import com.bytedance.myweather.viewmodel.WeatherViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyWeatherTheme {
                WeatherScreen(weatherViewModel) { navigateToToday() }
            }
        }
    }

    private fun navigateToToday() {
        val intent = Intent(this, TodayWeatherActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        startActivity(intent)
    }
}

@Composable
fun WeatherScreen(viewModel: WeatherViewModel, onNavigateToToday: () -> Unit) {
    val weatherData by viewModel.weatherData.collectAsState()
    val currentCity by viewModel.currentCity.collectAsState()

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
            if (weatherData?.forecasts != null) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "未来预报",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 30.dp)
                    )
                    Text(
                        text = weatherData!!.forecasts!![0].city,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    LazyColumn(modifier = Modifier.padding(top = 24.dp)) {
                        items(weatherData!!.forecasts!![0].casts) { cast ->
                            ForecastItem(cast = cast)
                        }
                    }
                }
            } else {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(text = "Loading...", color = Color.White, fontSize = 18.sp)
                }
            }
            BottomBar(
                viewModel = viewModel, 
                currentCity = currentCity, 
                onNavigateToForecast = { },
                onNavigateToToday = onNavigateToToday,
                isTodayScreen = false
            )
        }
    }
}

@Composable
fun ForecastItem(cast: Cast) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayOfWeek(dateString: String): String {
        return try {
            val date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
            date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINESE)
        } catch (e: Exception) {
            ""
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date Column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = getDayOfWeek(cast.date),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = cast.date.substring(5),
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
            // Weather Icon & Text
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.WbCloudy,
                    contentDescription = cast.dayweather,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = cast.dayweather,
                    color = Color.White,
                    fontSize = 15.sp,
                )
            }
            // Temperature
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                Text(
                    text = "${cast.daytemp}°",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${cast.nighttemp}°",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp
                )
            }
        }
    }
}