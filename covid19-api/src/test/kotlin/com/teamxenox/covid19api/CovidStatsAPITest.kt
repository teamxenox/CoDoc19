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
}