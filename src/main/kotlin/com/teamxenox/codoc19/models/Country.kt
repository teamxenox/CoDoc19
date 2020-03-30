package com.teamxenox.codoc19.models

import com.google.gson.annotations.SerializedName


class Country(
        @SerializedName("code")
        val code: String, // ZW
        @SerializedName("name")
        val name: String // Zimbabwe
)