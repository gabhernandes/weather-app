package com.interview.weather.details

data class GetWeatherForCityResponse(
    val currentWeather: CurrentWeather?,
    val predictedWeatherForDays: Map<String, PredictedWeather>,
)
