package com.bytedance.myweather.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bytedance.myweather.viewmodel.WeatherViewModel

@Composable
fun BottomBar(
    viewModel: WeatherViewModel,
    currentCity: String,
    onNavigateToForecast: () -> Unit,
    onNavigateToToday: () -> Unit,
    isTodayScreen: Boolean
) {
    val cities = listOf("北京", "武汉", "广州", "深圳")

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(bottom = 20.dp, top = 10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            cities.forEach { city ->
                Button(
                    onClick = { viewModel.changeCity(city) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (city == currentCity) Color(0xFF007AFF) else Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Text(text = city, color = Color.White)
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))) {
            Row(modifier = Modifier.padding(horizontal = 40.dp, vertical = 10.dp)) {
                Row(
                    modifier = Modifier.clickable { onNavigateToToday() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.LocationCity, contentDescription = "城市", tint = if(isTodayScreen) Color.White else Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("城市", color = if(isTodayScreen) Color.White else Color.Gray)
                }
                Spacer(modifier = Modifier.width(40.dp))
                Row(
                    modifier = Modifier.clickable { onNavigateToForecast() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.ShowChart, contentDescription = "预报", tint = if(!isTodayScreen) Color.White else Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("预报", color = if(!isTodayScreen) Color.White else Color.Gray)
                }
            }
        }
    }
}