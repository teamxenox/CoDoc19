package com.teamxenox.covid19api.models.jhu

data class JhuData(
        val countryName: String,
        val daySeries: List<Int>
)