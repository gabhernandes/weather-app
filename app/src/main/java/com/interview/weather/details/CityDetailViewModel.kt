package com.interview.weather.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.weather.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CityDetailUiState {
    data object Loading : CityDetailUiState
    data class Success(val weather: GetWeatherForCityResponse) : CityDetailUiState
    data object Error : CityDetailUiState
}

class CityDetailViewModel(
    private val cityName: String,
    private val repository: WeatherRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CityDetailUiState>(CityDetailUiState.Loading)
    val uiState: StateFlow<CityDetailUiState> = _uiState.asStateFlow()

    init {
        loadWeather()
    }

    fun loadWeather() {
        _uiState.value = CityDetailUiState.Loading
        viewModelScope.launch {
            _uiState.value = try {
                CityDetailUiState.Success(repository.getWeatherForCity(cityName))
            } catch (e: Exception) {
                CityDetailUiState.Error
            }
        }
    }
}
