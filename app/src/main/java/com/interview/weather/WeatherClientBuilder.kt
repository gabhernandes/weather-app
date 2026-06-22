package com.interview.weather

import com.interview.weather.details.CurrentWeather
import com.interview.weather.details.GetWeatherForCityResponse
import com.interview.weather.details.PredictedWeather
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.mock.Calls
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.Random
import java.util.concurrent.TimeUnit

object WeatherClientBuilder {

  // <editor-fold defaultstate="collapsed" desc="Only need to look at the client class">
  @JvmStatic
  fun build(): WeatherClient {
    val retrofit = Retrofit.Builder()
      .baseUrl("https://localhost/")
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
      .build()

    val mock = MockRetrofit.Builder(retrofit)
      .networkBehavior(NetworkBehavior
        .create(Random(System.currentTimeMillis())).apply {
//          setFailurePercent(10)
          setDelay(5, TimeUnit.SECONDS)
        }
      )
      .build()

    val delegate = mock.create(WeatherService::class.java)

    return WeatherClient(object: WeatherService {
      override fun fetchCitiesCall(): Call<GetCitiesResponse> {
        return delegate.returning(Calls.response(cities())).fetchCitiesCall()
      }

      override fun fetchCitiesReactive(): Single<GetCitiesResponse> {
        return delegate.returning(Calls.response(cities())).fetchCitiesReactive()
      }

      override suspend fun fetchCitiesSuspend(): GetCitiesResponse {
        return delegate.returning(Calls.response(cities())).fetchCitiesSuspend()
      }

      override fun fetchWeatherForCityCall(cityName: String): Call<GetWeatherForCityResponse> {
        return delegate.returning(Calls.response(weatherForCity())).fetchWeatherForCityCall(cityName)
      }

      override fun fetchWeatherForCityReactive(cityName: String): Single<GetWeatherForCityResponse> {
        return delegate.returning(Calls.response(weatherForCity())).fetchWeatherForCityReactive(cityName)
      }

      override suspend fun fetchWeatherForCitySuspend(cityName: String): GetWeatherForCityResponse {
        return delegate.returning(Calls.response(weatherForCity())).fetchWeatherForCitySuspend(cityName)
      }
    })
  }

  private fun cities() = GetCitiesResponse(countriesWithCities.entries
    .flatMap { entry -> entry.value.map { City(it, entry.key,
      90 * random.nextGaussian(), 180 * random.nextGaussian()) } }.shuffled())

  private fun weatherForCity(): GetWeatherForCityResponse {
    return GetWeatherForCityResponse(
      CurrentWeather(
        weatherDescriptions.random(),
        randomTemperatureCelsius(),
        (100 * random.nextFloat()).toInt().toShort(),
        (100 * random.nextFloat()).toInt().toShort(),
        "${1000 + 20 * random.nextGaussian()} mbar",
        windDirections.random(),
        100 * random.nextFloat(),
      ),
      mapOf(
        "Sunday" to randomPredictedWeather(),
        "Monday" to randomPredictedWeather(),
        "Tuesday" to randomPredictedWeather(),
        "Wednesday" to randomPredictedWeather(),
        "Thursday" to randomPredictedWeather(),
        "Friday" to randomPredictedWeather(),
        "Saturday" to randomPredictedWeather(),
      )
    )
  }

  private fun randomPredictedWeather(): PredictedWeather {
    val minTemp = randomTemperatureCelsius()
    val maxTemp = minTemp + 15 * random.nextFloat()

    return PredictedWeather(minTemp, maxTemp, weatherDescriptions.random())
  }

  private fun randomTemperatureCelsius(): Float = (10 + 20 * random.nextGaussian()).toFloat()

  private val random = Random(System.currentTimeMillis())

  private val countriesWithCities = mapOf(
    "Australia" to listOf("Canberra", "Melbourne", "Perth", "Sydney"),
    "Canada" to listOf("Toronto", "Vancouver", "Montreal", "Calgary"),
    "Ireland" to listOf("Dublin", "Galway", "Kilkenny"),
    "Mexico" to listOf("Guadalajara", "Mexico City", "Monterrey"),
    "Philippines" to listOf("Manila", "Cebu City"),
    "United Kingdom" to listOf("London", "Conwy", "Brighton", "Bath", "York", "Edinburgh"),
    "United States" to listOf("San Francisco", "New York", "Boston", "Los Angeles"),
    "Uruguay" to listOf("Montevideo", "Punta del Este", "Colonia del Sacramento"),
  )

  private val weatherDescriptions = listOf(
    "Cloudy with a chance of meatballs",
    "Mostly sunny with afternoon tornadoes",
    "Plague of locusts",
    "Hot as an oven",
    "Sideways rain",
  )

  private val windDirections = listOf(
    "North",
    "Northeast",
    "East",
    "Southeast",
    "South",
    "Southwest",
    "West",
    "Northwest",
  )
  // </editor-fold>
}