package com.teamxenox.covid19api.core

import com.teamxenox.covid19api.models.jhu.JhuParseData
import com.teamxenox.covid19api.utils.ArrayUtils

object JHUCSVParser {
    const val COUNTRY_GLOBAL = "Global"
    private const val TEXT_FIELD_COUNT_GLOBAL = 4
    private const val TEXT_FIELD_COUNT_USA = 12
    private const val INVALID_SK = "\"Korea, South\""
    private const val REPLACEMENT_SK = "South Korea"
    private val CSV_SPLIT_REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex()

    private val countryNameMap = mapOf(
            "s. korea" to "south korea",
            "usa" to "us"
    )


    /**
     * If country name == null, global data will be returned
     * part.first = first death start date
     */
    fun parseData(_countryName: String, csvData: String, isGlobal: Boolean): JhuParseData {

        var countryName = _countryName.toLowerCase()

        if (countryNameMap.containsKey(countryName)) {
            countryName = countryNameMap[countryName].toString()
        }

        val data = mutableListOf<List<Int>>()

        val countryGlobal = COUNTRY_GLOBAL.toLowerCase()
        val csvLines = csvData.split("\n")
        val headings = csvLines.first().split(",")
        var firstDeathDate: String? = null

        val textFieldCount = if (isGlobal) {
            TEXT_FIELD_COUNT_GLOBAL
        } else {
            TEXT_FIELD_COUNT_USA
        }

        val countryNameArrIndex = if (isGlobal) {
            1
        } else {
            7
        }

        println("Parsing $textFieldCount:$countryNameArrIndex...")
        for ((index, _line) in csvLines.withIndex()) {
            var line = _line

            if (index == 0) {
                continue
            }

            // south korea fix
            if (isGlobal) {
                if (line.contains(INVALID_SK)) {
                    line = line.replace(INVALID_SK, REPLACEMENT_SK)
                }
            }

            val fields = line.split(CSV_SPLIT_REGEX)
            if (fields.size > 1) {
                val fCountryName = fields[countryNameArrIndex].toLowerCase()
                if (fCountryName.contains("korea")) {
                    println("korea found")
                }
                if (countryName == countryGlobal || fCountryName == countryName) {
                    val deaths = fields.subList(textFieldCount, fields.size).map { it.toInt() }
                    if (firstDeathDate == null) {
                        // first death not found yet
                        val hasDeath = deaths.sum() > 0
                        if (hasDeath) {
                            val firstDeathIndex = deaths.indexOfFirst { it > 0 }
                            require(firstDeathIndex != -1) { "TSH : Death index can't be negative" }
                            firstDeathDate = headings[textFieldCount + firstDeathIndex]
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