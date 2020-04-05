package com.teamxenox.covid19api.models

import com.google.gson.annotations.SerializedName


data class IndianApiResponse(
        @SerializedName("cases_time_series")
        val casesTimeSeries: List<CasesTimeSery>,
        @SerializedName("key_values")
        val keyValues: List<KeyValue>,
        @SerializedName("statewise")
        val statewise: List<Statewise>,
        @SerializedName("tested")
        val tested: List<Tested>
) {
    data class CasesTimeSery(
            @SerializedName("dailyconfirmed")
            val dailyconfirmed: String, // 1
            @SerializedName("dailydeceased")
            val dailydeceased: String, // 0
            @SerializedName("dailyrecovered")
            val dailyrecovered: String, // 0
            @SerializedName("date")
            val date: String, // 30 January
            @SerializedName("totalconfirmed")
            val totalconfirmed: String, // 1
            @SerializedName("totaldeceased")
            val totaldeceased: String, // 0
            @SerializedName("totalrecovered")
            val totalrecovered: String // 0
    )

    data class KeyValue(
            @SerializedName("confirmeddelta")
            val confirmeddelta: String, // 24
            @SerializedName("counterforautotimeupdate")
            val counterforautotimeupdate: String, // 645
            @SerializedName("deceaseddelta")
            val deceaseddelta: String, // 3
            @SerializedName("lastupdatedtime")
            val lastupdatedtime: String, // 29/03/2020 16:17:24
            @SerializedName("recovereddelta")
            val recovereddelta: String, // 1
            @SerializedName("statesdelta")
            val statesdelta: String // 0
    )

    data class Statewise(
            @SerializedName("active")
            val active: String, // 940
            @SerializedName("confirmed")
            val confirmed: String, // 1053
            @SerializedName("deaths")
            val deaths: String, // 27

            @SerializedName("deltaconfirmed")
            val deltaconfirmed: Int, // 24
            @SerializedName("deltadeaths")
            val deltadeaths: Int, // 0
            @SerializedName("deltarecovered")
            val deltarecovered: Int, // 0

            @SerializedName("lastupdatedtime")
            val lastupdatedtime: String, // 29/03/2020 16:17:24
            @SerializedName("recovered")
            val recovered: String, // 86
            @SerializedName("state")
            val state: String // Total
    )

    data class Tested(
            @SerializedName("source")
            val source: String, // Press_Release_ICMR_13March2020.pdf
            @SerializedName("totalindividualstested")
            val totalindividualstested: String, // 5900
            @SerializedName("totalpositivecases")
            val totalpositivecases: String, // 78
            @SerializedName("totalsamplestested")
            val totalsamplestested: String, // 6500
            @SerializedName("updatetimestamp")
            val updatetimestamp: String // 13/3/2020 00:00
    )
}