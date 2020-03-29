package com.teamxenox.covid19api

import com.winterbe.expekt.should
import org.junit.Test

class CovidStatsAPITest {
    @Test
    fun testIndiaData() {
        val indiaStats = CovidStatsAPI.getStats("IND")
        indiaStats.totalDeaths.should.above(25)
    }

    @Test
    fun testOtherCountryData() {
        val spain = CovidStatsAPI.getStats("spain")
        spain.totalDeaths.should.above(6000)
    }
}