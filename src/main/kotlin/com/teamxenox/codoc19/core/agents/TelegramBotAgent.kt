package com.teamxenox.codoc19.core.agents

import com.teamxenox.bootzan.GsonUtils.gson
import com.teamxenox.codoc19.core.ContactManager
import com.teamxenox.codoc19.core.features.qa.FeedbackParser
import com.teamxenox.codoc19.core.SecretConstants
import com.teamxenox.codoc19.core.base.BotAgent
import com.teamxenox.codoc19.core.features.test.Doctor
import com.teamxenox.scholar.Scholar
import com.teamxenox.scholar.models.Answer
import com.teamxenox.scholar.subjects.corona.Corona
import com.teamxenox.telegramapi.Telegram
import com.teamxenox.telegramapi.models.*

class TelegramBotAgent : BotAgent {

    private var update: Update? = null
    private var feedbackQuery: CallbackQueryResponse? = null
    private val telegramApi = Telegram(SecretConstants.TELEGRAM_ACTIVE_BOT_TOKEN)
    private var chatId: Long = -1
    private var messageId: Long = -1
    private var doctor: Doctor? = null

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

        private const val CMD_HELP = "/help"
        private const val CMD_START = "/start"
        private const val CMD_TEST = "/test"
        private const val CMD_QUIZ = "/quiz"
        private const val CMD_UPDATE = "/update"
    }

    override fun handle(data: Any) {

        val jsonString = gson.toJson(data)

        if (isButtonClick(jsonString)) {
            handleButtonClick()
        } else {
            handleQuestion(jsonString)
        }
    }

    private fun handleButtonClick() {
        Thread {

            // Sending feedback to cancel progress animation
            val resp = telegramApi.answerCallbackQuery(
                    AnswerCallbackRequest(
                            feedbackQuery!!.callbackQuery.id
                    )
            )

            if (resp.code() == 200) {
                println("answerCallback success")
            } else {
                println("answerCallback failed")
            }

        }.start()

        val query = feedbackQuery!!.callbackQuery
        val buttonData = query.data

        println("Button click data is `$buttonData`")

        chatId = query.message.chat.id
        messageId = query.message.messageId

        doctor = Doctor(telegramApi, chatId, messageId)

        if (doctor!!.isTestButtonClick(buttonData)) {

            println("It's test button click")

            doctor!!.handle(buttonData)


        } else {
            println("It's feedback for scholar API")
            // question feedback for scholar API
            handleFeedback()
        }
    }


    override fun runQuiz() {
        sendToBeDone()
    }

    private fun sendToBeDone() {
        telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        text = "TO BE DONE! 👷"
                )
        )
    }

    override fun startTest() {
        doctor = Doctor(telegramApi, chatId, messageId)
        doctor!!.startTest()
    }


    override fun sendUpdate() {
        sendToBeDone()
    }


    private fun handleFeedback() {

        val query = feedbackQuery!!.callbackQuery
        val feedbackData = query.data

        // Sending typing
        sendTyping(feedbackQuery!!.callbackQuery.from.id)

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

    private fun handleQuestion(jsonString: String?) {

        this.update = gson.fromJson(jsonString, Update::class.java)
        val message = update!!.message.text

        // From information
        chatId = update!!.message.chat.id
        messageId = update!!.message.messageId

        sendTyping(chatId)

        println("Message is `$message`")

        if (message == CMD_HELP || message == CMD_START) {
            sendHelp(chatId, messageId)
        } else if (message == CMD_TEST) {
            startTest()
        } else if (message == CMD_QUIZ) {
            runQuiz()
        } else if (message == CMD_UPDATE) {
            sendUpdate()
        } else {
            val answer = Scholar.getAnswer(Corona, message)
            if (answer != null) {
                sendAnswer(chatId, messageId, answer)
            } else {
                sendDontKnow(chatId, messageId)
            }
        }
    }


    private fun sendTyping(chatId: Long) {
        val response = telegramApi.sendChatAction(
                SendChatActionRequest(Telegram.CHAT_ACTION_TYPING, chatId)
        )

        if (response.code() == 200) {
            println("Typing sent...")
        } else {
            println("Failed to send typing...")
        }
    }

    /**
     * To send answer
     */
    private fun sendAnswer(chatId: Long, replyId: Long, answer: Answer) {
        val answerBody = prepareAnswerBody(answer)
        telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        text = answerBody,
                        replyMsgId = replyId,
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
                        Q: ${answer.matchedQuestion}
                        A: ${answer.answer}
                        
                        💪 Answer Confidence : ${answer.confidence}% $emoji
                        🌍 Source : <a href="${answer.sourceLink}">${answer.source}</a>
                    """.trimIndent()
    }

    private fun sendDontKnow(chatId: Long, replyId: Long) {
        val resp = telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        text = "Sorry, I don't know about that 🤷",
                        replyMsgId = replyId
                )
        )
        if (resp.code() != 200) {
            println("Failed to send help")
        }
    }

    /**
     * To send help text
     */
    private fun sendHelp(chatId: Long, replyMsgId: Long) {
        val resp = telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        text = """
                            Hey, My name is CoDoc 👨‍⚕️. I am here to help you with COVID-19.
                            
                            🤖 Don't know much about COVID-19? Ask me your question
                            🥼  $CMD_TEST To check the likelihood of having COVID-19
                            🤔 $CMD_QUIZ Are you taking the correct protective measures against COVID-19 ? Find out
                            📈 $CMD_UPDATE To get global COVID-19 statistics
                            
                        """.trimIndent()
                )
        )
        if (resp.code() != 200) {
            println("Failed to send help")
        }
    }

    private fun isButtonClick(jsonString: String): Boolean {
        this.feedbackQuery = gson.fromJson(jsonString, CallbackQueryResponse::class.java)
        return this.feedbackQuery?.callbackQuery != null
    }


}