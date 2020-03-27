package com.teamxenox.codoc19.core.agents

import com.fasterxml.jackson.databind.ObjectMapper
import com.teamxenox.bootzan.GsonUtils
import com.teamxenox.codoc19.core.SecretConstants
import com.teamxenox.codoc19.core.base.BotAgent
import com.teamxenox.scholar.Scholar
import com.teamxenox.scholar.models.Answer
import com.teamxenox.scholar.subjects.corona.Corona
import com.teamxenox.telegramapi.Telegram
import com.teamxenox.telegramapi.models.SendMessageRequest
import com.teamxenox.telegramapi.models.Update
import java.lang.StringBuilder

class TelegramBotAgent : BotAgent {

    private val telegramApi = Telegram(SecretConstants.TELEGRAM_ACTIVE_BOT_TOKEN)

    companion object {

        // Feedback buttons
        private const val FEEDBACK_RELEVANT_TEXT = "✅ Relevant"
        private const val FEEDBACK_FAKE_TEXT = "😤 Fake!"
        private const val FEEDBACK_IRRELEVANT_TEXT = "😒 Irrelevant"
        private const val FEEDBACK_OUTDATED_TEXT = "📆 Outdated"

        private const val FEEDBACK_RELEVANT_KEY = 'r'
        private const val FEEDBACK_FAKE_KEY = 'f'
        private const val FEEDBACK_IRRELEVANT_KEY = 'i'
        private const val FEEDBACK_OUTDATED_KEY = 'o'

        private const val HELP_COMMAND = "/help"
        private const val START_COMMAND = "/start"
    }

    override fun handle(data: Any) {
        val gson = GsonUtils.gson
        val update = gson.fromJson(gson.toJson(data), Update::class.java)
        val message = update.message.text

        // From information
        val chatId = update.message.chat.id
        val replyId = update.message.messageId

        if (message == HELP_COMMAND || START_COMMAND == HELP_COMMAND) {
            sendHelp(chatId, replyId)
        } else {
            val answer = Scholar.getAnswer(Corona, message)
            if (answer != null) {
                sendAnswer(chatId, replyId, answer)
            } else {
                sendDontKnow(chatId, replyId)
            }
        }
    }



    private fun sendAnswer(chatId: Long, replyId: Long, answer: Answer) {
        val answerBody = prepareAnswerBody(answer)
        telegramApi.sendMessage(
                SendMessageRequest(
                        chatId,
                        answerBody,
                        true,
                        "HTML",
                        replyId,
                        getFeedbackButtons(answer)
                )
        )
    }

    private fun getFeedbackButtons(
            answer: Answer
    ): SendMessageRequest.ReplyMarkup? {
        return try {

            val modelAndQuestion = answer.documentId + answer.question

            SendMessageRequest.ReplyMarkup(
                    listOf(
                            listOf(
                                    SendMessageRequest.InlineButton(
                                            FEEDBACK_RELEVANT_TEXT,
                                            FEEDBACK_RELEVANT_KEY + modelAndQuestion
                                    ),

                                    SendMessageRequest.InlineButton(
                                            FEEDBACK_FAKE_TEXT,
                                            FEEDBACK_FAKE_KEY + modelAndQuestion
                                    )
                            ),
                            listOf(
                                    SendMessageRequest.InlineButton(
                                            FEEDBACK_IRRELEVANT_TEXT,
                                            FEEDBACK_IRRELEVANT_KEY + modelAndQuestion
                                    ),
                                    SendMessageRequest.InlineButton(
                                            FEEDBACK_OUTDATED_TEXT,
                                            FEEDBACK_OUTDATED_KEY + modelAndQuestion
                                    )
                            )
                    )
            )
        } catch (e: SendMessageRequest.InlineButton.ByteOverflowException) {
            e.printStackTrace()
            null
        }
    }

    private fun prepareAnswerBody(answer: Answer): String {
        val emoji = when (answer.confidence.toInt()) {
            in 0..30 -> "❤️" // red = average
            in 31..70 -> "🧡️" // orange = ok
            else -> "💚" // green = best
        }

        return """
                        Q: ${answer.question}
                        A: ${answer.answer}
                        
                        💪 Answer Confidence : ${answer.confidence}% $emoji
                        🌍 Source : <a href="${answer.sourceLink}">${answer.source}</a>
                    """.trimIndent()
    }

    private fun sendDontKnow(chatId: Long, replyId: Long) {
        val resp = telegramApi.sendMessage(
                SendMessageRequest(
                        chatId,
                        "Sorry, I don't know about that 🤷",
                        true,
                        "HTML",
                        replyId,
                        null
                )
        )
        if (resp.code() != 200) {
            println("Failed to send help")
        }
    }

    /**
     * To
     */
    private fun sendHelp(chatId: Long, replyMsgId: Long) {
        val resp = telegramApi.sendMessage(
                SendMessageRequest(
                        chatId,
                        "Hey, Welcome :)",
                        true,
                        "HTML",
                        replyMsgId,
                        null
                )
        )
        if (resp.code() != 200) {
            println("Failed to send help")
        }
    }


}