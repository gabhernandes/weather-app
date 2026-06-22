package com.interview.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.interview.weather.cities.CitiesScreen
import com.interview.weather.cities.CitiesViewModel
import com.interview.weather.data.NetworkWeatherRepository
import com.interview.weather.data.WeatherRepository
import com.interview.weather.details.CityDetailScreen
import com.interview.weather.details.CityDetailViewModel
import com.interview.weather.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {

    private val repository: WeatherRepository by lazy {
        NetworkWeatherRepository(WeatherClientBuilder.build())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                WeatherApp(repository)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeatherApp(repository: WeatherRepository) {
    var selectedCityName by rememberSaveable { mutableStateOf<String?>(null) }
    val currentCity = selectedCityName

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = currentCity ?: stringResource(R.string.cities_title))
                },
                navigationIcon = {
                    if (currentCity != null) {
                        IconButton(onClick = { selectedCityName = null }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        if (currentCity == null) {
            val citiesViewModel: CitiesViewModel = viewModel(
                factory = CitiesViewModelFactory(repository),
            )
            val state by citiesViewModel.uiState.collectAsState()
            CitiesScreen(
                state = state,
                onCityClick = { selectedCityName = it.name },
                onRetry = citiesViewModel::loadCities,
                modifier = Modifier.padding(innerPadding),
            )
        } else {
            BackHandler { selectedCityName = null }
            val detailViewModel: CityDetailViewModel = viewModel(
                key = currentCity,
                factory = CityDetailViewModelFactory(currentCity, repository),
            )
            val state by detailViewModel.uiState.collectAsState()
            CityDetailScreen(
                state = state,
                onRetry = detailViewModel::loadWeather,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
