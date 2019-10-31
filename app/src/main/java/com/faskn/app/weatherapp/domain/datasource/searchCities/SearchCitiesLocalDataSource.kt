package com.faskn.app.weatherapp.domain.datasource.searchCities

import com.faskn.app.weatherapp.db.dao.CitiesForSearchDao
import com.faskn.app.weatherapp.db.entity.CitiesForSearchEntity
import com.faskn.app.weatherapp.domain.model.SearchResponse
import javax.inject.Inject

/**
 * Created by Furkan on 2019-10-31
 */

class SearchCitiesLocalDataSource @Inject constructor(private val citiesForSearchDao: CitiesForSearchDao) {

    fun getCityByName(cityName: String) = citiesForSearchDao.getCityByName(cityName)

    fun insertCities(response: SearchResponse) {
        response.hits?.forEach {
            citiesForSearchDao.insertCity(CitiesForSearchEntity(it))
        }
    }
}
