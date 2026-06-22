package com.interview.weather.details

data class CurrentWeather(
    val longDescription: String,
    val feelsLikeCelsius: Float,
    val rainChancePercent: Short,
    val humidityPercent: Short,
    val airPressureDescription: String,
    val windDirection: String,
    val windSpeed: Float
)
