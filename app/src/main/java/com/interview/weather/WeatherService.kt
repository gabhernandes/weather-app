package com.interview.weather

import com.interview.weather.details.GetWeatherForCityResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

  // ========== Network calls for the city list screen ==========

  @GET("/cities")
  fun fetchCitiesCall(): Call<GetCitiesResponse>

  @GET("/cities")
  fun fetchCitiesReactive(): Single<GetCitiesResponse>

  @GET("/cities")
  suspend fun fetchCitiesSuspend(): GetCitiesResponse

  // ========== Network calls for the city forecast screen ==========

  @GET("/cities/{cityName}/weather")
  fun fetchWeatherForCityCall(@Path("cityName") cityName: String): Call<GetWeatherForCityResponse>

  @GET("/cities/{cityName}/weather")
  fun fetchWeatherForCityReactive(@Path("cityName") cityName: String): Single<GetWeatherForCityResponse>

  @GET("/cities/{cityName}/weather")
  suspend fun fetchWeatherForCitySuspend(@Path("cityName") cityName: String): GetWeatherForCityResponse
}