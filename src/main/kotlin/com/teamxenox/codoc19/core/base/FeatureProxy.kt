package com.teamxenox.codoc19.core.base

import com.teamxenox.telegramapi.Telegram
import com.teamxenox.telegramapi.models.SendChatActionRequest
import com.teamxenox.telegramapi.models.SendMessageRequest

abstract class FeatureProxy(
        private val telegramApi: Telegram,
        private val chatId: Long,
        private val messageId: Long
) {
    abstract fun handle(jsonString: String)

    protected fun sendTyping() {

        val response = telegramApi.sendChatAction(
                SendChatActionRequest(Telegram.CHAT_ACTION_TYPING, chatId)
        )

        if (response.code() == 200) {
            println("Typing sent...")
        } else {
            println("Failed to send typing...")
        }
    }

    fun sendError(msg: String) {
        telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        text = "😥 $msg",
                        replyMsgId = messageId
                )
        )
    }

}