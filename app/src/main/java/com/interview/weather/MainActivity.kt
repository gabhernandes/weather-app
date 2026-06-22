package com.interview.weather

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.interview.weather.ui.theme.WeatherAppTheme
import java.io.IOException

class MainActivity : ComponentActivity() {

    private val weatherNetworkClient: WeatherClient = WeatherClientBuilder.build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var cities: List<City>
        try {
            cities = weatherNetworkClient.waitForCities()!!.cities
        } catch (e : IOException) {
            Log.e(MainActivity::class.java.simpleName, "Error on waitForCities", e)
            cities = emptyList()
        }

        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CitiesView(
                        cities = cities,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CitiesView(cities: List<City>, modifier: Modifier = Modifier) {
    LazyColumn {
        items(cities) {
            Text(
                text = it.name,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        CitiesView(
            listOf(
                City("London", "UK", 0.0, 0.0),
                City("New York", "USA", 0.0, 0.0),
            )
        )
    }
}