package com.teamxenox.covid19api

import com.teamxenox.covid19api.models.Statistics
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CovidStatsAPI {

    private val indianApi by lazy {
        return@lazy Retrofit.Builder()
                .baseUrl("https://api.covid19india.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IndianApi::class.java)
    }

    private val globalApi by lazy {
        return@lazy Retrofit.Builder()
                .baseUrl("https://corona.lmao.ninja/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GlobalApi::class.java)
    }

    fun getStats(_country: String): Statistics {
        val country = _country.toLowerCase()
        if (country == "ind" || country == "india") {

            val indianApiResponse = indianApi.getData().execute().body()!!
            val india = indianApiResponse.statewise.find { it.state == "Total" }!!

            return Statistics(
                    india.confirmed.toInt(),
                    india.deaths.toInt(),
                    india.recovered.toInt(),
                    india.active.toInt(),
                    india.delta.confirmed,
                    india.delta.deaths
            )

        } else {
            val countryData = globalApi.getCountryData(country).execute().body()!!
            return Statistics(
                    countryData.cases,
                    countryData.deaths,
                    countryData.recovered,
                    countryData.active,
                    countryData.todayCases,
                    countryData.todayDeaths
            )
        }
    }

}