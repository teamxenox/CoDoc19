package com.teamxenox.covid19api.models

import com.google.gson.annotations.SerializedName


data class GlobalAllResponse(
        @SerializedName("active")
        val active: Int, // 490164
        @SerializedName("cases")
        val cases: Int, // 662967
        @SerializedName("deaths")
        val deaths: Int, // 30850
        @SerializedName("recovered")
        val recovered: Int, // 141953
        @SerializedName("updated")
        val updated: Long // 1585442840490
)