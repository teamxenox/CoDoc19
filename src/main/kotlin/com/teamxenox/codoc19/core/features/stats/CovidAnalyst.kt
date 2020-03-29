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
            send(globalStats, "🗺 World")
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

    private fun send(stats: Statistics, header: String) {

        val txt = toText(header, stats)

        telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        replyMsgId = messageId,
                        text = txt
                )
        )
    }

    private fun toText(header: String, globalStats: Statistics): String {
        return """
            👉 $header

            😷 ${globalStats.totalCases.get("Case", "Cases")} : <b>${addComma(globalStats.totalCases)}</b>
            😥 ${globalStats.totalDeaths.get("Death", "Deaths")} : <b>${addComma(globalStats.totalDeaths)}</b>
            😇 Recovered : <b>${addComma(globalStats.totalRecovered)}</b>
            🏥 Active ${globalStats.totalActiveCases.get("Case", "Cases")} : <b>${addComma(globalStats.totalActiveCases)}</b>
            
            😷 Today ${globalStats.todayCases.get("Case", "Cases")} : <b>${addComma(globalStats.todayCases)}</b>
            😥 Today ${globalStats.todayDeaths.get("Death", "Deaths")} : <b>${addComma(globalStats.todayDeaths)}</b>
            
            
        """.trimIndent()
    }

    fun sendCountryStats(country: String) {
        val countryStats = CovidStatsAPI.getStats(country)

        if (countryStats != null) {
            send(countryStats, "📍 $country")
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