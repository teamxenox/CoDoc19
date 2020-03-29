package com.teamxenox.covid19api.models

import com.google.gson.annotations.SerializedName


data class GlobalCountryResponse(
        @SerializedName("active")
        val active: Int, // 117986
        @SerializedName("cases")
        val cases: Int, // 123428
        @SerializedName("casesPerOneMillion")
        val casesPerOneMillion: Float, // 373
        @SerializedName("country")
        val country: String, // USA
        @SerializedName("countryInfo")
        val countryInfo: CountryInfo,
        @SerializedName("critical")
        val critical: Int, // 2666
        @SerializedName("deaths")
        val deaths: Int, // 2211
        @SerializedName("deathsPerOneMillion")
        val deathsPerOneMillion: Float, // 7
        @SerializedName("recovered")
        val recovered: Int, // 3231
        @SerializedName("todayCases")
        val todayCases: Int, // 19302
        @SerializedName("todayDeaths")
        val todayDeaths: Int // 515
) {
    data class CountryInfo(
            @SerializedName("country")
            val country: String, // USA
            @SerializedName("flag")
            val flag: String, // https://raw.githubusercontent.com/NovelCOVID/API/master/assets/flags/us.png
            @SerializedName("_id")
            val id: Int, // 840
            @SerializedName("iso2")
            val iso2: String, // US
            @SerializedName("iso3")
            val iso3: String, // USA
            @SerializedName("lat")
            val lat: Double, // 38
            @SerializedName("long")
            val long: Double // -97
    )
}