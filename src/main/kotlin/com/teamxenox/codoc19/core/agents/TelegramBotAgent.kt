package com.teamxenox.codoc19.core.agents

import com.teamxenox.bootzan.JacksonUtils
import com.teamxenox.codoc19.core.base.BotAgent
import com.teamxenox.telegramapi.models.Update

class TelegramBotAgent : BotAgent {
    override fun handle(data: Any) {
        val update = JacksonUtils.cast(data, Update::class.java)

    }
}