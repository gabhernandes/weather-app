package com.interview.weather

import com.interview.weather.cities.CitiesUiState
import com.interview.weather.cities.CitiesViewModel
import com.interview.weather.data.WeatherRepository
import com.interview.weather.details.GetWeatherForCityResponse
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CitiesViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun city(name: String, country: String) = City(name, country, 0.0, 0.0)

    @Test
    fun `emits Loading then Success with grouped sorted cities`() = runTest {
        val repository = FakeWeatherRepository(
            cities = listOf(
                city("New York", "United States"),
                city("London", "United Kingdom"),
                city("Boston", "United States"),
            ),
        )

        val viewModel = CitiesViewModel(repository)

        assertEquals(CitiesUiState.Loading, viewModel.uiState.value)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is CitiesUiState.Success)
        val sections = (state as CitiesUiState.Success).sections
        assertEquals(listOf("United Kingdom", "United States"), sections.map { it.countryName })
        assertEquals(
            listOf("Boston", "New York"),
            sections.first { it.countryName == "United States" }.cities.map { it.name },
        )
    }

    @Test
    fun `emits Error when the repository fails`() = runTest {
        val viewModel = CitiesViewModel(FakeWeatherRepository(shouldThrow = true))

        advanceUntilIdle()

        assertEquals(CitiesUiState.Error, viewModel.uiState.value)
    }

    @Test
    fun `retry reloads after a failure`() = runTest {
        val repository = FakeWeatherRepository(
            cities = listOf(city("Dublin", "Ireland")),
            shouldThrow = true,
        )
        val viewModel = CitiesViewModel(repository)
        advanceUntilIdle()
        assertEquals(CitiesUiState.Error, viewModel.uiState.value)

        repository.shouldThrow = false
        viewModel.loadCities()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is CitiesUiState.Success)
    }

    @Test
    fun `state is Loading until the network call completes`() = runTest {
        val gate = CompletableDeferred<Unit>()
        val repository = FakeWeatherRepository(
            cities = listOf(city("Galway", "Ireland")),
            gate = gate,
        )
        val viewModel = CitiesViewModel(repository)

        advanceUntilIdle()
        assertEquals(CitiesUiState.Loading, viewModel.uiState.value)

        gate.complete(Unit)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value is CitiesUiState.Success)
    }

    private class FakeWeatherRepository(
        private val cities: List<City> = emptyList(),
        var shouldThrow: Boolean = false,
        private val gate: CompletableDeferred<Unit>? = null,
    ) : WeatherRepository {

        override suspend fun getCities(): List<City> {
            gate?.await()
            if (shouldThrow) throw RuntimeException("network error")
            return cities
        }

        override suspend fun getWeatherForCity(cityName: String): GetWeatherForCityResponse =
            throw NotImplementedError()
    }
}
