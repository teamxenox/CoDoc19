package com.teamxenox.codoc19.core.features.stats

import com.teamxenox.codoc19.core.base.FeatureProxy
import com.teamxenox.codoc19.data.entities.Chart
import com.teamxenox.codoc19.data.entities.User
import com.teamxenox.codoc19.data.repos.ChartRepo
import com.teamxenox.codoc19.utils.StringUtils.addComma
import com.teamxenox.codoc19.utils.get
import com.teamxenox.covid19api.CovidStatsAPI
import com.teamxenox.covid19api.chart.Graphologist
import com.teamxenox.covid19api.models.Statistics
import com.teamxenox.telegramapi.Telegram
import com.teamxenox.telegramapi.models.SendMessageRequest
import com.teamxenox.telegramapi.models.SendPhotoRequest
import java.text.SimpleDateFormat
import java.util.*

class CovidAnalyst(private val telegramApi: Telegram, private val chatId: Long, private val messageId: Long) : FeatureProxy(telegramApi, chatId, messageId) {

    companion object {
        private const val CHART_REQUEST_PREFIX = "cr"
        private const val CHART_DEATH = "CD"
        private const val CHART_CASE = "CS"
        private const val CHART_RECOVERED = "CR"
        private const val CHART_DEATH_DAILY = "CDD"
        private const val CHART_CASE_DAILY = "CCD"
        private const val CHART_RECOVERED_DAILY = "CRD"
        private val CHART_REQUEST_REGEX = "$CHART_REQUEST_PREFIX(?<chartType>$CHART_DEATH_DAILY|$CHART_CASE_DAILY|$CHART_RECOVERED_DAILY|$CHART_DEATH|$CHART_CASE|$CHART_RECOVERED)(?<countryName>.+)".toRegex()
    }

    override fun handle(jsonString: String) {
        TODO("Not yet implemented")
    }

    fun sendGlobalStats() {
        val globalStats = CovidStatsAPI.getGlobalStats()
        if (globalStats != null) {
            send(globalStats, "🗺 <b>World</b>", true)
        } else {
            telegramApi.sendMessage(
                    SendMessageRequest(
                            chatId = chatId,
                            replyMsgId = messageId,
                            text = "Something went wrong while processing the request. Please try again later"
                    )
            )
        }
    }

    private fun send(stats: Statistics, header: String, isGlobal: Boolean) {

        val txt = toText(header, stats, isGlobal)
        val countryName = stats.countryName
        val chartButtons = mutableListOf(
                listOf(
                        SendMessageRequest.InlineButton("DEATHS 📈", "${CHART_REQUEST_PREFIX}${CHART_DEATH}$countryName"),
                        SendMessageRequest.InlineButton("CASES 📉", "${CHART_REQUEST_PREFIX}${CHART_CASE}$countryName"),
                        SendMessageRequest.InlineButton("RECOVERED 📉", "${CHART_REQUEST_PREFIX}${CHART_RECOVERED}$countryName")
                ),
                listOf(
                        SendMessageRequest.InlineButton("D-DAILY 📈", "${CHART_REQUEST_PREFIX}${CHART_DEATH_DAILY}$countryName"),
                        SendMessageRequest.InlineButton("C-DAILY 📉", "${CHART_REQUEST_PREFIX}${CHART_CASE_DAILY}$countryName"),
                        SendMessageRequest.InlineButton("R-DAILY 📉", "${CHART_REQUEST_PREFIX}${CHART_RECOVERED_DAILY}$countryName")
                )
        )

        telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        replyMsgId = messageId,
                        text = txt,
                        replyMarkup = SendMessageRequest.ReplyMarkup(chartButtons)
                )
        )
    }

    private fun toText(header: String, stats: Statistics, isGlobal: Boolean): String {

        val globalText = if (stats.countryName != "India") {
            "🌐 Statistics are based on GMT +00:00"
        } else {
            ""
        }

        return """
            $header

            😷 ${stats.totalCases.get("Case", "Cases")} : <b>${addComma(stats.totalCases)}</b>
            😥 ${stats.totalDeaths.get("Death", "Deaths")} : <b>${addComma(stats.totalDeaths)}</b>
            😇 Recovered : <b>${addComma(stats.totalRecovered)}</b>
            🏥 Active ${stats.totalActiveCases.get("Case", "Cases")} : <b>${addComma(stats.totalActiveCases)}</b>
            
            😷 Today ${stats.todayCases.get("Case", "Cases")} : <b>${addComma(stats.todayCases)}</b>
            😥 Today ${stats.todayDeaths.get("Death", "Deaths")} : <b>${addComma(stats.todayDeaths)}</b>
            
            $globalText
        """.trimIndent()
    }

    fun sendCountryStats(country: String) {
        val countryStats = CovidStatsAPI.getStats(country)

        if (countryStats != null) {
            send(countryStats, "📍 <b>${countryStats.countryName}</b>", false)
        } else {
            telegramApi.sendMessage(
                    SendMessageRequest(
                            chatId = chatId,
                            replyMsgId = messageId,
                            text = "Ohh sorry. We don't have enough data from `$country` 😥"
                    )
            )
        }
    }

    fun isChartRequest(buttonData: String): Boolean {
        println("Pattern : `${CHART_REQUEST_REGEX.pattern}`")
        println("Data : `$buttonData`")
        return buttonData.matches(CHART_REQUEST_REGEX)
    }

    private val normalDateFormat = SimpleDateFormat("dd-MM-yyyy")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val mySqlDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    fun sendChart(user: User, buttonData: String, chartRepo: ChartRepo) {

        val match = CHART_REQUEST_REGEX.find(buttonData)!!
        val type = match.groups["chartType"]!!.value
        val countryName = match.groups["countryName"]!!.value

        when (type) {

            CHART_DEATH -> {
                sendChart(
                        user,
                        chartRepo,
                        countryName,
                        Chart.Type.DEATH,
                        Graphologist.CHART_DEATH
                )
            }

            CHART_CASE -> {
                sendChart(
                        user,
                        chartRepo,
                        countryName,
                        Chart.Type.CASE,
                        Graphologist.CHART_CASE
                )
            }

            CHART_DEATH_DAILY -> {
                sendChart(
                        user,
                        chartRepo,
                        countryName,
                        Chart.Type.DEATH_DAILY,
                        Graphologist.CHART_DEATH_DAILY
                )
            }

            CHART_CASE_DAILY -> {
                sendChart(
                        user,
                        chartRepo,
                        countryName,
                        Chart.Type.CASE_DAILY,
                        Graphologist.CHART_CASE_DAILY
                )
            }

            CHART_RECOVERED -> {
                sendChart(
                        user,
                        chartRepo,
                        countryName,
                        Chart.Type.RECOVERED,
                        Graphologist.CHART_RECOVERED
                )
            }

            CHART_RECOVERED_DAILY -> {
                sendChart(
                        user,
                        chartRepo,
                        countryName,
                        Chart.Type.RECOVERED_DAILY,
                        Graphologist.CHART_RECOVERED_DAILY
                )
            }

            else -> {
                throw IllegalArgumentException("Undefined chart type `$type`")
            }
        }
    }

    private fun sendChart(
            user: User,
            chartRepo: ChartRepo,
            countryName: String,
            chartType: Chart.Type,
            gChartType: Int
    ) {

        // checking if the chart available
        val chartDate = Date()
        val dateString = dateFormat.format(chartDate)
        val normalDate = normalDateFormat.format(chartDate)
        val exChart = chartRepo.getChartCountryDateType(countryName, dateString, chartType)

        val caption = when (chartType) {

            Chart.Type.DEATH -> {
                "📈 Deaths as of $normalDate"
            }

            Chart.Type.CASE -> {
                "📉 Confirmed cases as of $normalDate"
            }

            Chart.Type.RECOVERED -> {
                "📉 Recovered cases as of $normalDate"
            }

            Chart.Type.DEATH_DAILY -> {
                "📈 Deaths each day until $normalDate"
            }

            Chart.Type.CASE_DAILY -> {
                "📈 Confirmed cases each day until $normalDate"
            }

            Chart.Type.RECOVERED_DAILY -> {
                "📈 Recovered cases each day until $normalDate"
            }

        } + " - $countryName"


        if (exChart == null) {

            println("Chat doesn't exist creating new one")

            val jhuData = when (chartType) {
                Chart.Type.RECOVERED, Chart.Type.RECOVERED_DAILY -> {
                    CovidStatsAPI.getRecoveredData(countryName)
                }

                Chart.Type.DEATH, Chart.Type.DEATH_DAILY -> {
                    CovidStatsAPI.getDeathData(countryName)
                }

                Chart.Type.CASE, Chart.Type.CASE_DAILY -> {
                    CovidStatsAPI.getCaseData(countryName)
                }
            }

            if (jhuData != null) {

                val chartFile = Graphologist().prepareChart(gChartType, dateString, jhuData)
                val sendFileResp = telegramApi.sendPhotoFile(
                        chatId,
                        chartFile,
                        caption
                )

                if (sendFileResp.code() == 200) {

                    // chart sent
                    val sendPhotoResp = sendFileResp.body()!!
                    // delete chart from server
                    chartFile.delete()

                    // add the file id to db to reuser
                    val fileId = sendPhotoResp.result.photo.first().fileId
                    val newChart = Chart().apply {
                        userId = user.id
                        tgFileId = fileId
                        this.chartType = chartType
                        country = countryName
                        createdAt = mySqlDateFormat.format(chartDate)
                    }

                    println("Chart saved to db : ${chartRepo.save(newChart)}")

                } else {
                    sendError("Something went wrong while sending the chart to you. Please try later")
                }
            } else {
                sendError("Sorry charts are not available for `$countryName` 🤷‍♂")
            }
        } else {
            // chart already exist, just sent it
            println("Chart exist. reusing...")

            telegramApi.sendPhoto(SendPhotoRequest(
                    chatId = chatId,
                    photo = exChart.tgFileId,
                    caption = caption
            ))

            println("Chart send")
        }
    }
}