# Weather App

Small weather app built on top of the provided starter. It lists cities grouped by country and
lets you tap one to see its forecast. Written in Kotlin with Jetpack Compose, and the mock network
service is left as it was.

## What it does

- Cities are grouped under their country with headers. Countries and the cities inside them are
  sorted alphabetically (ignoring case).
- Tapping a city opens a detail screen that calls `getWeatherForCity` and shows the current
  conditions (feels like, rain chance, humidity, pressure, wind) plus a 7-day forecast.
- Both screens show a spinner while loading and a retry button if the call fails.

## How it's put together

I went with MVVM since it makes the network and state handling easy to test. The ViewModels
(`CitiesViewModel`, `CityDetailViewModel`) expose a `StateFlow` of UI state, and everything goes
through a `WeatherRepository` interface so the ViewModels don't know or care that the data comes
from the mock client. The actual grouping and sorting lives in a plain function
(`groupCitiesByCountry`) so I could test it on its own without spinning up anything.

Navigation is just a piece of state (which city is selected) rather than pulling in the Navigation
Compose library. For a two screen app that felt like enough.

## Running it

Open in Android Studio and run the `app` config, or:

```bash
./gradlew assembleDebug
```

## Tests

```bash
./gradlew testDebugUnitTest
```

There are two test classes worth looking at:

- `CityGroupingTest` covers the grouping and sorting rules, including case-insensitivity and the
  empty case.
- `CitiesViewModelTest` uses a fake repository to check the loading, success, and error flow and
  retry, including that it stays in Loading until the call actually finishes.

## Notes on the submission

**What I built:** the grouped city list (required), the unit tests (required), and a few of the
bonuses, namely MVVM with a repository so things are testable, loading and error states, and the
city detail screen.

**A couple of judgment calls:**

- The mockup shows Sydney above Melbourne, which isn't alphabetical. The README says sort
  alphabetically, so I followed the README and treated the mockups as rough guides.
- The detail screen is a straightforward functional layout rather than a match for the mockup. I
  focused on wiring up the real `getWeatherForCity` data and the loading and error handling instead
  of styling.
- I swapped the sticky headers for normal header rows, since the sticky API wasn't resolving on this
  Compose version and it isn't needed for the requirement.

**If I had more time:** I'd add a proper navigation setup, cache responses behind the repository so
it isn't refetching on every trip into the detail screen, and write some Compose UI tests on top of
the unit tests. Dependency injection (Hilt) would also be nicer than the hand-rolled ViewModel
factories. I'd also tidy up a few raw mock values, like the air pressure that currently prints with
a long decimal tail.

**Rough edges:** the detail screen works but isn't styled to the mockup, the air pressure value
shows unrounded, there's no caching so it refetches every time, and the tests are focused on the
logic that matters rather than chasing full coverage.
