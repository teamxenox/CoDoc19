package com.teamxenox.codoc19.core

import com.teamxenox.codoc19.core.agents.TelegramBotAgent
import com.teamxenox.codoc19.core.base.BotAgent
import com.teamxenox.codoc19.data.repos.AnalyticsRepo
import com.teamxenox.codoc19.data.repos.ChartRepo
import com.teamxenox.codoc19.data.repos.UserRepo
import java.lang.IllegalArgumentException

class BotsManager(
        private val userRepo: UserRepo,
        private val analyticsRepo: AnalyticsRepo,
        private val chartRepo: ChartRepo
) {

    companion object {
        // Agent Keys
        private const val AGENT_TELEGRAM = "telegram"
    }

    fun getAgentOrThrow(agentKey: String): BotAgent {
        return when (agentKey) {
            AGENT_TELEGRAM -> TelegramBotAgent(userRepo, analyticsRepo, chartRepo)
            else -> throw IllegalArgumentException("Undefined agent : `$agentKey`")
        }
    }

}