package com.interview.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.interview.weather.cities.CitiesViewModel
import com.interview.weather.data.WeatherRepository
import com.interview.weather.details.CityDetailViewModel

class CitiesViewModelFactory(
    private val repository: WeatherRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CitiesViewModel(repository) as T
}

class CityDetailViewModelFactory(
    private val cityName: String,
    private val repository: WeatherRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CityDetailViewModel(cityName, repository) as T
}
