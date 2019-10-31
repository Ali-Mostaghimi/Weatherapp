package com.faskn.app.weatherapp.repo

import NetworkBoundResource
import androidx.lifecycle.LiveData
import com.faskn.app.weatherapp.db.entity.CitiesForSearchEntity
import com.faskn.app.weatherapp.domain.datasource.searchCities.SearchCitiesLocalDataSource
import com.faskn.app.weatherapp.domain.datasource.searchCities.SearchCitiesRemoteDataSource
import com.faskn.app.weatherapp.domain.model.SearchResponse
import com.faskn.app.weatherapp.utils.domain.RateLimiter
import com.faskn.app.weatherapp.utils.domain.Resource
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Furkan on 2019-10-31
 */

class SearchCitiesRepository @Inject constructor(private val searchCitiesLocalDataSource: SearchCitiesLocalDataSource, private val searchCitiesRemoteDataSource: SearchCitiesRemoteDataSource) {

    private val currentWeatherRateLimit = RateLimiter<String>(30, TimeUnit.SECONDS)

    fun loadCitiesByCityName(cityName: String): LiveData<Resource<List<CitiesForSearchEntity>>> {
        return object : NetworkBoundResource<List<CitiesForSearchEntity>, SearchResponse>() {
            override fun saveCallResult(item: SearchResponse) = searchCitiesLocalDataSource.insertCities(item)

            override fun shouldFetch(data: List<CitiesForSearchEntity>?): Boolean = data?.size!! < 1

            override fun loadFromDb(): LiveData<List<CitiesForSearchEntity>> = searchCitiesLocalDataSource.getCityByName(cityName)

            override fun createCall(): Single<SearchResponse> = searchCitiesRemoteDataSource.getCityWithQuery(cityName)

            override fun onFetchFailed() = currentWeatherRateLimit.reset(RATE_LIMITER_TYPE)
        }.asLiveData
    }

    companion object {
        private const val RATE_LIMITER_TYPE = "data"
    }
}
