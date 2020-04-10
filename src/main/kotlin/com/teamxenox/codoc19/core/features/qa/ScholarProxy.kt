package com.teamxenox.codoc19.core.features.qa

import com.teamxenox.bootzan.GsonUtils
import com.teamxenox.codoc19.core.base.FeatureProxy
import com.teamxenox.scholar.Scholar
import com.teamxenox.scholar.models.Answer
import com.teamxenox.scholar.subjects.corona.Corona
import com.teamxenox.telegramapi.Telegram
import com.teamxenox.telegramapi.models.CallbackQueryResponse
import com.teamxenox.telegramapi.models.SendMessageRequest
import com.teamxenox.telegramapi.models.Update

class ScholarProxy(
        private val telegramApi: Telegram,
        private val chatId: Long,
        private val messageId: Long
) : FeatureProxy(telegramApi, chatId, messageId) {

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
    }

    private var update: Update? = null

    /**
     * To handle the request
     */
    override fun handle(jsonString: String) {

        this.update = GsonUtils.gson.fromJson(jsonString, Update::class.java)
        val message = update!!.message!!.text

        val answer = Scholar.getAnswer(Corona, message)
        if (answer != null) {
            sendAnswer(answer)
        } else {
            sendDontKnow()
        }
    }

    /**
     * To prepare the message body
     */
    private fun prepareAnswerBody(answer: Answer): String {
        val emoji = when (answer.confidence.toInt()) {
            in 0..30 -> "❤️" // red = average
            in 31..70 -> "🧡️" // orange = ok
            else -> "💚" // green = best
        }

        return """
            Q: ${answer.matchedQuestion}
A: ${answer.answer}
            
            💪 Answer Confidence : ${answer.confidence}% $emoji
            🌍 Source : <a href="${answer.sourceLink}">${answer.source}</a>""".trimIndent()
    }

    /**
     * To send answer
     */
    private fun sendAnswer(answer: Answer) {
        val answerBody = prepareAnswerBody(answer)
        telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        text = answerBody,
                        replyMsgId = messageId,
                        replyMarkup = getFeedbackButtons(answer)
                )
        )
    }

    /**
     * To get feedback buttons for the given answer
     */
    private fun getFeedbackButtons(
            answer: Answer
    ): SendMessageRequest.ReplyMarkup? {
        return try {

            val modelAndQuestion = answer.documentId + answer.askedQuestion

            SendMessageRequest.ReplyMarkup(
                    listOf(
                            listOf(
                                    // Relevant
                                    SendMessageRequest.InlineButton(
                                            FEEDBACK_RELEVANT_TEXT,
                                            FEEDBACK_RELEVANT_KEY + modelAndQuestion
                                    ),

                                    // Fake
                                    SendMessageRequest.InlineButton(
                                            FEEDBACK_FAKE_TEXT,
                                            FEEDBACK_FAKE_KEY + modelAndQuestion
                                    )
                            ),
                            listOf(
                                    // Irrelevant
                                    SendMessageRequest.InlineButton(
                                            FEEDBACK_IRRELEVANT_TEXT,
                                            FEEDBACK_IRRELEVANT_KEY + modelAndQuestion
                                    ),

                                    // Outdated
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


    /**
     * To send `Sorry, I don't know about that`
     */
    private fun sendDontKnow() {
        val resp = telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        text = "Sorry, I don't know about that 🤷",
                        replyMsgId = messageId
                )
        )
        if (resp.code() != 200) {
            println("Failed to send help")
        }
    }


    /**
     * To handle the feedback button click
     */
    fun handleFeedback(feedbackQuery: CallbackQueryResponse) {

        val query = feedbackQuery.callbackQuery
        val feedbackData = query.data

        // Sending typing
        sendTyping()

        val feedback = FeedbackParser.parse(feedbackData)
        Scholar.addFeedback(Corona, feedback)


        // Sending thanks
        telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        text = "Thank you for your feedback 🤗",
                        replyMsgId = messageId
                )
        )
    }


}