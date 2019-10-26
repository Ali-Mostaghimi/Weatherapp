package com.faskn.app.weatherapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.faskn.app.weatherapp.core.BaseViewModel
import com.faskn.app.weatherapp.db.entity.CurrentWeatherEntity
import com.faskn.app.weatherapp.db.entity.ForecastEntity
import com.faskn.app.weatherapp.domain.usecase.CurrentWeatherUseCase
import com.faskn.app.weatherapp.domain.usecase.ForecastUseCase
import com.faskn.app.weatherapp.utils.domain.Resource
import javax.inject.Inject

/**
 * Created by Furkan on 2019-10-24
 */

class DashboardFragmentViewModel @Inject internal constructor(private val forecastUseCase: ForecastUseCase, private val currentWeatherUseCase: CurrentWeatherUseCase) : BaseViewModel() {

    private var forecastLiveData: LiveData<ForecastViewState> = MutableLiveData()
    private var currentWeatherLiveData: LiveData<CurrentWeatherViewState> = MutableLiveData()

    fun getForecastLiveData() = forecastLiveData
    fun getCurrentWeatherLiveData() = currentWeatherLiveData

    init {
        getForecast(ForecastUseCase.ForecastParams("Istanbul,TR", true, "metric"))
        getCurrentWeather((CurrentWeatherUseCase.CurrentWeatherParams("Istanbul,TR", true, "metric")))
    }

    private fun getForecast(params: ForecastUseCase.ForecastParams): LiveData<ForecastViewState> {
        forecastLiveData =
            Transformations.switchMap(
                forecastUseCase.execute(params)
            ) {
                val forecastLiveData = MutableLiveData<ForecastViewState>()
                forecastLiveData.value = onForecastResultReady(it)
                return@switchMap forecastLiveData
            }

        return forecastLiveData
    }

    private fun getCurrentWeather(params: CurrentWeatherUseCase.CurrentWeatherParams): LiveData<CurrentWeatherViewState> {
        currentWeatherLiveData = Transformations.switchMap(
            currentWeatherUseCase.execute(params)
        ) {
            val currentWeatherLiveData = MutableLiveData<CurrentWeatherViewState>()
            currentWeatherLiveData.value = onCurrentWeatherResultReady(it)
            return@switchMap currentWeatherLiveData
        }
        return currentWeatherLiveData
    }

    private fun onForecastResultReady(resource: Resource<ForecastEntity>): ForecastViewState {
        return ForecastViewState(
            status = resource.status,
            error = resource.message,
            data = resource.data
        )
    }

    private fun onCurrentWeatherResultReady(resource: Resource<CurrentWeatherEntity>): CurrentWeatherViewState {
        return CurrentWeatherViewState(
            status = resource.status,
            error = resource.message,
            data = resource.data
        )
    }
}
