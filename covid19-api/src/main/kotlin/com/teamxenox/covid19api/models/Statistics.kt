package com.teamxenox.covid19api.models

data class Statistics(
        val totalCases: Int,
        val totalDeaths: Int,
        val totalRecovered: Int,
        val totalActiveCases: Int,
        val todayCases: Int,
        val todayDeaths: Int
)