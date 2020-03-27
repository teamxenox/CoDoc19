package com.teamxenox.codoc19.core

import com.teamxenox.codoc19.core.agents.TelegramBotAgent
import com.teamxenox.codoc19.core.base.BotAgent
import java.lang.IllegalArgumentException

object BotsManager {

    // Agent Keys
    private const val AGENT_TELEGRAM = "telegram"


    fun getAgentOrThrow(agentKey: String): BotAgent {
        return when (agentKey) {
            AGENT_TELEGRAM -> TelegramBotAgent()
            else -> throw IllegalArgumentException("Undefined agent : `$agentKey`")
        }
    }

}