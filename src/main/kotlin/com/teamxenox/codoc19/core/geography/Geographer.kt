package com.teamxenox.codoc19.core.geography

import com.google.gson.reflect.TypeToken
import com.teamxenox.bootzan.GsonUtils
import com.teamxenox.codoc19.models.Country
import com.teamxenox.codoc19.utils.JarUtils
import java.io.File

object Geographer {

    private val countries by lazy {
        val countriesFile = File("${JarUtils.getJarDir()}assets/countries.json")
        val type = object : TypeToken<List<Country>>() {}.type
        GsonUtils.gson.fromJson<List<Country>>(countriesFile.readText(), type)
    }

    fun getCountry(_text: String): Country? {
        val text = _text.toLowerCase().trim()
        for (country in countries) {
            if (country.name.toLowerCase() == text || country.code.toLowerCase() == text) {
                return country
            }
        }
        return null
    }
}