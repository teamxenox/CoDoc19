package com.teamxenox.codoc19.models

import com.google.gson.annotations.SerializedName


data class Contact(
        @SerializedName("number")
        val number: String, // 9436055743
        @SerializedName("state")
        val state: String // Arunachal Pradesh
)