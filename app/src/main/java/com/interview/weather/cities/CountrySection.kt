package com.interview.weather.cities

import com.interview.weather.City

data class CountrySection(
    val countryName: String,
    val cities: List<City>,
)

fun groupCitiesByCountry(cities: List<City>): List<CountrySection> =
    cities
        .groupBy { it.countryName }
        .map { (country, citiesInCountry) ->
            CountrySection(
                countryName = country,
                cities = citiesInCountry.sortedBy { it.name.lowercase() },
            )
        }
        .sortedBy { it.countryName.lowercase() }
