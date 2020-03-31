package com.teamxenox.covid19api.models

data class RoyLabData(
        val id: Int,
        val countryName: String,
        val confirmedCases: Int,
        val deaths: Int,
        val recovered: Int
)