package com.teamxenox.covid19api.core

import com.teamxenox.covid19api.models.RoyLabData
import okhttp3.OkHttpClient
import okhttp3.Request

object RoyLab {

    private const val DATA_URL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vQuDj0R6K85sdtI8I-Tc7RCx8CnIxKUQue0TCUdrFOKDw9G3JRtGhl64laDd3apApEvIJTdPFJ9fEUL/pubhtml?gid=0&single=true"
    private val DATA_REGEX = "<div class=\"row-header-wrapper\" style=\"line-height: 20px;\">\\d+<\\/div><\\/th><td class=\"s0\">(?<countryId>\\d+)<\\/td><td class=\"s0\">(?<countryName>.+?)<\\/td><td class=\"s1\">(?<confirmedCases>\\d+)<\\/td><td class=\"s1\">(?<deaths>\\d+)<\\/td><td class=\"s1\">(?<recovered>\\d+)<\\/td><\\/tr>".toRegex()


    fun getData(): List<RoyLabData> {
        val client = OkHttpClient.Builder()
                .build()

        val request = Request.Builder()
                .url(DATA_URL)
                .build()

        val resp = client.newCall(request).execute().body()
        val htmlString = removeNewLinesAndMultipleSpaces(resp!!.string())
        val matches = DATA_REGEX.findAll(htmlString)
        val data = mutableListOf<RoyLabData>()
        for (match in matches) {
            val royData = RoyLabData(
                    match.groups["countryId"]!!.value.toInt(),
                    match.groups["countryName"]!!.value,
                    match.groups["confirmedCases"]!!.value.toInt(),
                    match.groups["deaths"]!!.value.toInt(),
                    match.groups["recovered"]!!.value.toInt()
            )

            data.add(royData)
        }
        return data
    }

    fun removeNewLinesAndMultipleSpaces(input: String): String {
        return input.replace("\n", "").replace("\\s{2,}".toRegex(), " ")
    }
}