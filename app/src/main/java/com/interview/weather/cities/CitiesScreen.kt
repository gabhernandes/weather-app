package com.interview.weather.cities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.interview.weather.City
import com.interview.weather.R
import com.interview.weather.ui.theme.WeatherAppTheme

@Composable
fun CitiesScreen(
    state: CitiesUiState,
    onCityClick: (City) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is CitiesUiState.Loading -> LoadingState(modifier)
        is CitiesUiState.Error -> ErrorState(
            message = stringResource(R.string.cities_error_message),
            onRetry = onRetry,
            modifier = modifier,
        )

        is CitiesUiState.Success -> GroupedCitiesList(
            sections = state.sections,
            onCityClick = onCityClick,
            modifier = modifier,
        )
    }
}

@Composable
private fun GroupedCitiesList(
    sections: List<CountrySection>,
    onCityClick: (City) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        sections.forEach { section ->
            item(key = section.countryName) {
                CountryHeader(section.countryName)
            }
            items(section.cities, key = { "${section.countryName}-${it.name}" }) { city ->
                CityRow(city = city, onClick = { onCityClick(city) })
            }
        }
    }
}

@Composable
private fun CountryHeader(countryName: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = countryName,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

@Composable
private fun CityRow(city: City, onClick: () -> Unit) {
    Column {
        Text(
            text = city.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 16.dp),
        )
        HorizontalDivider()
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onRetry, modifier = Modifier.padding(top = 16.dp)) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun CitiesScreenPreview() {
    WeatherAppTheme {
        CitiesScreen(
            state = CitiesUiState.Success(
                groupCitiesByCountry(
                    listOf(
                        City("New York", "USA", 0.0, 0.0),
                        City("Boston", "USA", 0.0, 0.0),
                        City("London", "UK", 0.0, 0.0),
                    )
                )
            ),
            onCityClick = {},
            onRetry = {},
        )
    }
}
