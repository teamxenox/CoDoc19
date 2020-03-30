package com.teamxenox.codoc19.core.features.stats

import com.teamxenox.codoc19.core.base.FeatureProxy
import com.teamxenox.codoc19.utils.StringUtils.addComma
import com.teamxenox.codoc19.utils.get
import com.teamxenox.covid19api.CovidStatsAPI
import com.teamxenox.covid19api.models.Statistics
import com.teamxenox.telegramapi.Telegram
import com.teamxenox.telegramapi.models.SendMessageRequest

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
                                                SendMessageRequest.InlineButton("SHOW DEATHS CHART 📈", "scDC$countryName")
                                        ),
                                        listOf(
                                                SendMessageRequest.InlineButton("SHOW CASES CHART 📈", "scCC$countryName")
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
        return buttonData.matches(CHART_REQUEST_REGEX)
    }
}