package com.teamxenox.covid19api

import com.winterbe.expekt.should
import org.junit.Test

class CovidStatsAPITest {

    @Test
    fun testGlobalData() {
        val globalStats = CovidStatsAPI.getGlobalStats()
        globalStats!!.totalDeaths.should.above(1000)
    }

    @Test
    fun testIndiaData() {
        val indiaStats = CovidStatsAPI.getStats("IND")
        indiaStats!!.totalDeaths.should.above(25)
    }

    @Test
    fun testInvalidCountry() {
        val indiaStats = CovidStatsAPI.getStats("invalidCountry")
        indiaStats.should.equal(null)
    }

    @Test
    fun testOtherCountryData() {
        val spain = CovidStatsAPI.getStats("spain")
        spain!!.totalDeaths.should.above(6000)
    }

    @Test
    fun testTimeSeriesDeathDataIndiaSuccess() {
        val deathData = CovidStatsAPI.getDeathData("India")!!
        deathData.countryName.should.equal("India")
        deathData.daySeries.size.should.above(0)
    }

    @Test
    fun testTimeSeriesCaseDataIndiaSuccess() {
        val deathData = CovidStatsAPI.getCaseData("India")!!
        println(deathData)
        deathData.countryName.should.equal("India")
        deathData.daySeries.size.should.above(0)
    }
}