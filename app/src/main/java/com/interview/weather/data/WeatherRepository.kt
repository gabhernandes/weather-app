package com.interview.weather.data

import com.interview.weather.City
import com.interview.weather.WeatherClient
import com.interview.weather.details.GetWeatherForCityResponse

interface WeatherRepository {
    suspend fun getCities(): List<City>
    suspend fun getWeatherForCity(cityName: String): GetWeatherForCityResponse
}

class NetworkWeatherRepository(
    private val client: WeatherClient,
) : WeatherRepository {

    override suspend fun getCities(): List<City> = client.fetchCitiesSuspend().cities

    override suspend fun getWeatherForCity(cityName: String): GetWeatherForCityResponse =
        client.fetchWeatherSuspend(cityName)
}
