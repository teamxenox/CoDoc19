package com.teamxenox.covid19api.core

import com.teamxenox.covid19api.utils.ArrayUtils
import java.lang.IllegalArgumentException

object JHUCSVParser {
    /**
     * If country name == null, global data will be returned
     */
    fun parseData(_countryName: String?, csvData: String): List<Int> {
        val countryName = _countryName?.toLowerCase()
        val data = mutableListOf<List<Int>>()

        for ((index, _line) in csvData.split("\n").withIndex()) {
            var line = _line
            if (index == 0) {
                continue
            }

            // south korea fix
            if (line.contains("Korea, South")) {
                line = line.replace("Korea, South", "South Korea")
            }

            val fields = line.split(",")
            if (fields.size > 1) {
                val fCountryName = fields[1].toLowerCase()
                if (fCountryName == countryName || countryName == null) {
                    val x = fields.subList(4, fields.size).map { it.toInt() }
                    data.add(x)
                }
            }

        }

        val xLen = data.first().size
        for (x in data) {
            if (x.size != xLen) {
                throw IllegalArgumentException("Data mismatch!")
            }
        }

        return ArrayUtils.mergeColumn(data)
    }
}