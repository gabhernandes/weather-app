package com.interview.weather

import com.interview.weather.details.GetWeatherForCityResponse
import retrofit2.Callback

@Suppress("unused")
class WeatherClient(private val service: WeatherService) {

  fun waitForCities(): GetCitiesResponse? = service.fetchCitiesCall().execute().body()

  fun fetchCities(callback: Callback<GetCitiesResponse>) {
    service.fetchCitiesCall().enqueue(callback)
  }

  fun fetchCities() = service.fetchCitiesReactive()

  suspend fun fetchCitiesSuspend() = service.fetchCitiesSuspend()

  fun waitForWeather(cityName: String): GetWeatherForCityResponse? =
    service.fetchWeatherForCityCall(cityName).execute().body()

  fun fetchWeather(cityName: String, callback: Callback<GetWeatherForCityResponse>) {
    service.fetchWeatherForCityCall(cityName).enqueue(callback)
  }

  fun fetchWeather(cityName: String) = service.fetchWeatherForCityReactive(cityName)

  suspend fun fetchWeatherSuspend(cityName: String) = service.fetchWeatherForCitySuspend(cityName)
}