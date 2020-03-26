package com.teamxenox.codoc19.core

import com.teamxenox.codoc19.core.agents.TelegramBotAgent
import com.teamxenox.codoc19.core.base.BotAgent
import java.lang.IllegalArgumentException

object BotsManager {

    // Agent Keys
    private const val AGENT_TELEGRAM = "telegram"

    // Registered bot agents
    private val agentMap = mapOf<String, BotAgent>(
            AGENT_TELEGRAM to TelegramBotAgent()
            // TODO: Add more bots here. Future plan!
    )

    fun getAgentOrThrow(agentKey: String): BotAgent {
        return agentMap[agentKey] ?: throw IllegalArgumentException("Invalid agent key  : `$agentKey`")
    }

}