package com.teamxenox.codoc19.core.features.stats

import com.teamxenox.codoc19.core.base.FeatureProxy
import com.teamxenox.codoc19.utils.StringUtils.addComma
import com.teamxenox.codoc19.utils.get
import com.teamxenox.covid19api.CovidStatsAPI
import com.teamxenox.covid19api.models.Statistics
import com.teamxenox.telegramapi.Telegram
import com.teamxenox.telegramapi.models.SendMessageRequest

class CovidAnalyst(private val telegramApi: Telegram, private val chatId: Long, private val messageId: Long) : FeatureProxy(telegramApi, chatId, messageId) {

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

        telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        replyMsgId = messageId,
                        text = txt
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
            send(countryStats, "📍 <b>$country</b>", false)
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
}