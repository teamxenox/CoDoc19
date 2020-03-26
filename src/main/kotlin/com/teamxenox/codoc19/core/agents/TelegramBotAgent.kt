package com.teamxenox.codoc19.core.agents

import com.fasterxml.jackson.databind.ObjectMapper
import com.teamxenox.bootzan.JacksonUtils
import com.teamxenox.codoc19.core.SecretConstants
import com.teamxenox.codoc19.core.base.BotAgent
import com.teamxenox.telegramapi.Telegram
import com.teamxenox.telegramapi.models.SendMessageRequest
import com.teamxenox.telegramapi.models.Update

class TelegramBotAgent : BotAgent {

    private val telegramApi = Telegram(SecretConstants.TELEGRAM_ACTIVE_BOT_TOKEN)

    companion object {
        private const val HELP_COMMAND = "/help"
        private const val START_COMMAND = "/start"
    }

    override fun handle(data: Any) {
        val update = JacksonUtils.cast(data, Update::class.java)
        val message = update.message!!.text
        println("Message is `$message`")
        if (message == HELP_COMMAND || START_COMMAND == HELP_COMMAND) {
            sendHelp(update)
        }
    }

    private fun sendHelp(update: Update) {
        println("Sending help...")
        val resp = telegramApi.sendMessage(
                SendMessageRequest(
                        update.message!!.chat!!.id!!,
                        "Hey, Welcome :)",
                        true,
                        "HTML",
                        update.message!!.messageId!!,
                        null
                )
        )
        println("->" + ObjectMapper().writeValueAsString(resp))
    }
}