package com.interview.weather

import com.interview.weather.cities.groupCitiesByCountry
import org.junit.Assert.assertEquals
import org.junit.Test

class CityGroupingTest {

    private fun city(name: String, country: String) = City(name, country, 0.0, 0.0)

    @Test
    fun `groups cities under their country`() {
        val cities = listOf(
            city("London", "United Kingdom"),
            city("New York", "United States"),
            city("York", "United Kingdom"),
        )

        val sections = groupCitiesByCountry(cities)

        assertEquals(2, sections.size)
        assertEquals(
            listOf("London", "York"),
            sections.first { it.countryName == "United Kingdom" }.cities.map { it.name },
        )
        assertEquals(
            listOf("New York"),
            sections.first { it.countryName == "United States" }.cities.map { it.name },
        )
    }

    @Test
    fun `sorts countries alphabetically`() {
        val cities = listOf(
            city("Sydney", "Australia"),
            city("Toronto", "Canada"),
            city("Dublin", "Ireland"),
            city("Boston", "United States"),
        )

        val countries = groupCitiesByCountry(cities).map { it.countryName }

        assertEquals(listOf("Australia", "Canada", "Ireland", "United States"), countries)
    }

    @Test
    fun `sorts cities alphabetically within a country regardless of input order`() {
        val cities = listOf(
            city("Sydney", "Australia"),
            city("Canberra", "Australia"),
            city("Perth", "Australia"),
            city("Melbourne", "Australia"),
        )

        val sortedNames = groupCitiesByCountry(cities).single().cities.map { it.name }

        assertEquals(listOf("Canberra", "Melbourne", "Perth", "Sydney"), sortedNames)
    }

    @Test
    fun `sorting is case insensitive`() {
        val cities = listOf(
            city("banana", "Country"),
            city("Apple", "Country"),
            city("cherry", "Country"),
        )

        val sortedNames = groupCitiesByCountry(cities).single().cities.map { it.name }

        assertEquals(listOf("Apple", "banana", "cherry"), sortedNames)
    }

    @Test
    fun `empty input produces no sections`() {
        assertEquals(emptyList<Any>(), groupCitiesByCountry(emptyList()))
    }
}
