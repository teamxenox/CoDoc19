package com.teamxenox.covid19api

import com.teamxenox.covid19api.apis.GlobalApi
import com.teamxenox.covid19api.apis.IndianApi
import com.teamxenox.covid19api.core.JHUCSVParser
import com.teamxenox.covid19api.core.RoyLab
import com.teamxenox.covid19api.models.GlobalCountryResponse
import com.teamxenox.covid19api.models.RoyLabData
import com.teamxenox.covid19api.models.Statistics
import com.teamxenox.covid19api.models.jhu.JhuData
import com.teamxenox.covid19api.utils.ArrayUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CovidStatsAPI {

    private const val DEATH_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv"
    private const val CASE_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv"
    private const val RECOVERED_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv"

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

    /**
     * To get stats by country
     */
    fun getStats(_country: String): Statistics? {

        val countryName = _country.toLowerCase()

        if (countryName == "ind" || countryName == "in" || countryName == "india") {

            val indianApiResponse = indianApi.getData().execute().body()!!
            val india = indianApiResponse.statewise.find { it.state == "Total" }!!

            return Statistics(
                    "India",
                    india.confirmed.toInt(),
                    india.deaths.toInt(),
                    india.recovered.toInt(),
                    india.active.toInt(),
                    india.delta.confirmed,
                    india.delta.deaths
            )

        } else {

            val resp = globalApi.getCountryData(countryName).execute()
            if (resp.code() == 200) {
                val countryData = resp.body()!!

                val royCountryData = RoyLab.getData().find { it.countryName.toLowerCase() == countryName }
                if (royCountryData != null) {
                    println("Roydata available")
                    return Statistics(
                            countryData.country,
                            largestOf(countryData.cases, royCountryData.confirmedCases),
                            largestOf(countryData.deaths, royCountryData.deaths),
                            largestOf(countryData.recovered, royCountryData.recovered),
                            countryData.active,
                            countryData.todayCases,
                            countryData.todayDeaths
                    )
                } else {
                    println("Roydata not available")
                    return Statistics(
                            countryData.country,
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

        return null
    }

    private fun largestOf(x: Int, y: Int): Int {
        return if (x > y) {
            x
        } else {
            y
        }
    }

    /**
     * To get global stats
     */
    fun getGlobalStats(): Statistics? {
        val globalAll = globalApi.getAll().execute().body()!!
        val resp = globalApi.getAllCountries().execute()
        if (resp.code() == 200) {
            val countriesAll = resp.body()!!
            var todayCases = 0
            var todayDeaths = 0

            for (country in countriesAll) {
                todayCases += country.todayCases
                todayDeaths += country.todayDeaths
            }

            val royLabData = RoyLab.getData()
            var rDeaths: Int = 0
            var rCases: Int = 0
            var rRecovered: Int = 0

            for (rData in royLabData) {
                rDeaths += rData.deaths
                rCases += rData.confirmedCases
                rRecovered += rData.recovered
            }

            return Statistics(
                    JHUCSVParser.COUNTRY_GLOBAL,
                    largestOf(globalAll.cases, rCases),
                    largestOf(globalAll.deaths, rDeaths),
                    largestOf(globalAll.recovered, rRecovered),
                    globalAll.active,
                    todayCases,
                    todayDeaths
            )
        }

        return null
    }

    fun getDeathData(countryName: String): JhuData? {
        return getJHUData(countryName, DEATH_DATA_URL)
    }


    fun getCaseData(countryName: String): JhuData? {
        return getJHUData(countryName, CASE_DATA_URL)
    }

    fun getRecoveredData(countryName: String): JhuData? {
        return getJHUData(countryName, RECOVERED_DATA_URL)
    }


    private fun getJHUData(_countryName: String, deathDataUrl: String): JhuData? {

        val countryName = _countryName.toLowerCase().trim()

        if (countryName == "us" || countryName == "usa" || countryName == "united states" || countryName == "united states of america") {
            // See : https://github.com/teamxenox/CoDoc19/issues/19
            return null
        } else {


            val client = OkHttpClient.Builder()
                    .build()

            val request = Request.Builder()
                    .url(deathDataUrl)
                    .build()

            val response = client.newCall(request).execute().body()
            if (response != null) {
                val csvData = response.string()
                val parsedData = JHUCSVParser.parseData(countryName, csvData)
                if (parsedData.deaths.isNotEmpty()) {
                    val finalData = ArrayUtils.trimStartNonDeaths(JHUCSVParser.merge(parsedData.deaths))
                    if (finalData.isNotEmpty()) {
                        return JhuData(
                                countryName,
                                parsedData.firstDeathDate,
                                finalData
                        )
                    }
                }
            }
        }

        return null
    }


}

