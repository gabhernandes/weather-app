package com.interview.weather.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.weather.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CitiesUiState {
    data object Loading : CitiesUiState
    data class Success(val sections: List<CountrySection>) : CitiesUiState
    data object Error : CitiesUiState
}

class CitiesViewModel(
    private val repository: WeatherRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CitiesUiState>(CitiesUiState.Loading)
    val uiState: StateFlow<CitiesUiState> = _uiState.asStateFlow()

    init {
        loadCities()
    }

    fun loadCities() {
        _uiState.value = CitiesUiState.Loading
        viewModelScope.launch {
            _uiState.value = try {
                CitiesUiState.Success(groupCitiesByCountry(repository.getCities()))
            } catch (e: Exception) {
                CitiesUiState.Error
            }
        }
    }
}
