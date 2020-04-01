package com.teamxenox.codoc19.core.geography

import com.google.gson.reflect.TypeToken
import com.teamxenox.bootzan.GsonUtils
import com.teamxenox.codoc19.models.Country

object Geographer {

    private val countries by lazy {
        val countriesJson = Geographer::class.java.getResourceAsStream("/countries.json")
                .bufferedReader()
                .readText()
        val type = object : TypeToken<List<Country>>() {}.type
        GsonUtils.gson.fromJson<List<Country>>(countriesJson, type)
    }

    fun getCountry(_text: String): Country? {
        val text = _text.toLowerCase().trim()
        for (country in countries) {
            if (
                    country.name.toLowerCase() == text ||
                    country.code.toLowerCase() == text ||
                    country.commonNames.find { it.toLowerCase() == text } != null
            ) {
                return country
            }
        }
        return null
    }
}