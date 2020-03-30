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
        private val CHART_REQUEST_REGEX = "$CHART_REQUEST_PREFIX(?<chartType>$CHART_DEATH|$CHART_CASE)(?<countryName>.+)".toRegex()
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
        telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        replyMsgId = messageId,
                        text = txt,
                        replyMarkup = SendMessageRequest.ReplyMarkup(
                                listOf(
                                        listOf(
                                                SendMessageRequest.InlineButton("DEATHS CHART 📈", "${CHART_REQUEST_PREFIX}${CHART_DEATH}$countryName"),
                                                SendMessageRequest.InlineButton("CASES CHART 📉", "${CHART_REQUEST_PREFIX}${CHART_CASE}$countryName")
                                        )
                                )
                        )
                )
        )
    }

    private fun toText(header: String, globalStats: Statistics, isGlobal: Boolean): String {

        val globalText = if (isGlobal) {
            "🌐 Global statistics are based on GMT +00:00"
        } else {
            ""
        }

        return """
            $header

            😷 ${globalStats.totalCases.get("Case", "Cases")} : <b>${addComma(globalStats.totalCases)}</b>
            😥 ${globalStats.totalDeaths.get("Death", "Deaths")} : <b>${addComma(globalStats.totalDeaths)}</b>
            😇 Recovered : <b>${addComma(globalStats.totalRecovered)}</b>
            🏥 Active ${globalStats.totalActiveCases.get("Case", "Cases")} : <b>${addComma(globalStats.totalActiveCases)}</b>
            
            😷 Today ${globalStats.todayCases.get("Case", "Cases")} : <b>${addComma(globalStats.todayCases)}</b>
            😥 Today ${globalStats.todayDeaths.get("Death", "Deaths")} : <b>${addComma(globalStats.todayDeaths)}</b>
            
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
                            text = "It seems like `$country` is not a country at all. 🙄"
                    )
            )
        }
    }

    fun isChartRequest(buttonData: String): Boolean {
        println("Pattern : `${CHART_REQUEST_REGEX.pattern}`")
        println("Data : `$buttonData`")
        return buttonData.matches(CHART_REQUEST_REGEX)
    }

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
        val exChart = chartRepo.getChartCountryDateType(countryName, dateString, chartType)

        val caption = if (chartType == Chart.Type.DEATH) {
            "Deaths as of $dateString"
        } else {
            "Confirmed cases as of $dateString"
        } + " - $countryName"


        if (exChart == null) {
            println("Chat doesn't exist creating new one")
            val jhuData = if (chartType == Chart.Type.DEATH) {
                CovidStatsAPI.getDeathData(countryName)
            } else {
                CovidStatsAPI.getCaseData(countryName)
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
                sendError("Uhh ho! the data is being synced. Please try again later")
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