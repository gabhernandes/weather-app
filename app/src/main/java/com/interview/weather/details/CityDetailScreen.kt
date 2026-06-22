package com.interview.weather.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.interview.weather.R
import com.interview.weather.ui.theme.WeatherAppTheme

@Composable
fun CityDetailScreen(
    state: CityDetailUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is CityDetailUiState.Loading -> Centered(modifier) { CircularProgressIndicator() }
        is CityDetailUiState.Error -> Centered(modifier) {
            Text(
                text = stringResource(R.string.detail_error_message),
                style = MaterialTheme.typography.bodyLarge,
            )
            Button(onClick = onRetry, modifier = Modifier.padding(top = 16.dp)) {
                Text(text = stringResource(R.string.retry))
            }
        }

        is CityDetailUiState.Success -> WeatherContent(state.weather, modifier)
    }
}

@Composable
private fun WeatherContent(weather: GetWeatherForCityResponse, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        weather.currentWeather?.let { CurrentWeatherCard(it) }
        if (weather.predictedWeatherForDays.isNotEmpty()) {
            ForecastCard(weather.predictedWeatherForDays)
        }
    }
}

@Composable
private fun CurrentWeatherCard(current: CurrentWeather) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.current_weather_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(text = current.longDescription, style = MaterialTheme.typography.bodyLarge)
            HorizontalDivider()
            DetailRow(
                stringResource(R.string.feels_like_label),
                stringResource(R.string.temperature_celsius, current.feelsLikeCelsius),
            )
            DetailRow(
                stringResource(R.string.rain_chance_label),
                stringResource(R.string.percent, current.rainChancePercent.toInt()),
            )
            DetailRow(
                stringResource(R.string.humidity_label),
                stringResource(R.string.percent, current.humidityPercent.toInt()),
            )
            DetailRow(stringResource(R.string.air_pressure_label), current.airPressureDescription)
            DetailRow(
                stringResource(R.string.wind_label),
                stringResource(R.string.wind_value, current.windDirection, current.windSpeed),
            )
        }
    }
}

@Composable
private fun ForecastCard(days: Map<String, PredictedWeather>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.forecast_title),
                style = MaterialTheme.typography.titleMedium,
            )
            days.forEach { (day, prediction) ->
                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = day, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = prediction.shortDescription,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Text(
                        text = stringResource(
                            R.string.temperature_range,
                            prediction.minTempCelsius,
                            prediction.maxTempCelsius,
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun Centered(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        content()
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun CityDetailScreenPreview() {
    WeatherAppTheme {
        CityDetailScreen(
            state = CityDetailUiState.Success(
                GetWeatherForCityResponse(
                    currentWeather = CurrentWeather(
                        longDescription = "Sideways rain",
                        feelsLikeCelsius = 12f,
                        rainChancePercent = 80,
                        humidityPercent = 65,
                        airPressureDescription = "1012 mbar",
                        windDirection = "Northwest",
                        windSpeed = 18f,
                    ),
                    predictedWeatherForDays = mapOf(
                        "Monday" to PredictedWeather(8f, 14f, "Cloudy"),
                        "Tuesday" to PredictedWeather(10f, 17f, "Sunny"),
                    ),
                )
            ),
            onRetry = {},
        )
    }
}
