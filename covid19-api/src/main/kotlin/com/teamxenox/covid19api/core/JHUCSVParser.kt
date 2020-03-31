package com.teamxenox.covid19api.core

import com.teamxenox.covid19api.models.jhu.JhuParseData
import com.teamxenox.covid19api.utils.ArrayUtils
import java.lang.IllegalArgumentException

object JHUCSVParser {
    const val COUNTRY_GLOBAL = "Global"
    private const val TEXT_FIELD_COUNT = 4
    private const val INVALID_SK = "\"Korea, South\""

    private val countryNameMap = mapOf(
            "s. korea" to "south korea"
    )


    /**
     * If country name == null, global data will be returned
     * part.first = first death start date
     */
    fun parseData(_countryName: String, csvData: String): JhuParseData {

        var countryName = _countryName.toLowerCase()

        if (countryNameMap.containsKey(countryName)) {
            countryName = countryNameMap[countryName].toString()
        }

        val data = mutableListOf<List<Int>>()

        val countryGlobal = COUNTRY_GLOBAL.toLowerCase()
        val csvLines = csvData.split("\n")
        val headings = csvLines.first().split(",")
        var firstDeathDate: String? = null
        for ((index, _line) in csvLines.withIndex()) {
            var line = _line

            if (index == 0) {
                continue
            }

            // south korea fix
            if (line.contains(INVALID_SK)) {
                line = line.replace(INVALID_SK, "South Korea")
            }

            val fields = line.split(",")
            if (fields.size > 1) {
                val fCountryName = fields[1].toLowerCase()
                if (countryName == countryGlobal || fCountryName == countryName) {
                    // skip first four params, ie state, country, lat and lon
                    val deaths = fields.subList(TEXT_FIELD_COUNT, fields.size).map { it.toInt() }
                    if (firstDeathDate == null) {
                        // first death not found yet
                        val hasDeath = deaths.sum() > 0
                        if (hasDeath) {
                            val firstDeathIndex = deaths.indexOfFirst { it > 0 }
                            require(firstDeathIndex != -1) { "TSH : Death index can't be negative" }
                            firstDeathDate = headings[TEXT_FIELD_COUNT + firstDeathIndex]
                        }
                    }

                    data.add(deaths)
                }
            }
        }


        return JhuParseData(
                firstDeathDate,
                data
        )
    }

    fun merge(data: List<List<Int>>): List<Int> {
        val xLen = data.first().size
        for (x in data) {
            if (x.size != xLen) {
                throw IllegalArgumentException("Data mismatch!")
            }
        }

        return ArrayUtils.mergeColumn(data)
    }


}