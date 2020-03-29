package com.teamxenox.codoc19.core.agents

import com.teamxenox.bootzan.GsonUtils.gson
import com.teamxenox.codoc19.core.SecretConstants
import com.teamxenox.codoc19.core.base.BotAgent
import com.teamxenox.codoc19.core.features.qa.ScholarProxy
import com.teamxenox.codoc19.core.features.quiz.QuizBoss
import com.teamxenox.codoc19.core.features.test.Doctor
import com.teamxenox.telegramapi.Telegram
import com.teamxenox.telegramapi.models.*

open class TelegramBotAgent : BotAgent {

    private var update: Update? = null
    private var feedbackQuery: CallbackQueryResponse? = null
    private val telegramApi = Telegram(SecretConstants.TELEGRAM_ACTIVE_BOT_TOKEN)
    private var chatId: Long = -1
    private var messageId: Long = -1
    private var doctor: Doctor? = null
    private var quizBoss: QuizBoss? = null

    companion object {
        private const val CMD_HELP = "/help"
        private const val CMD_START = "/start"
        private const val CMD_TEST = "/test"
        private const val CMD_QUIZ = "/quiz"
        private const val CMD_STATS = "/stats"
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
        quizBoss = QuizBoss(telegramApi, chatId, messageId)

        when {
            doctor!!.isTestButtonClick(buttonData) -> {

                println("It's test button click")
                doctor!!.handle(buttonData)

            }
            quizBoss!!.isQuizClickData(buttonData) -> {
                println("It's a quiz click")
                quizBoss!!.handle(buttonData)
            }

            else -> {
                println("It's feedback for scholar API")
                // question feedback for scholar API
                val scholarProxy = ScholarProxy(telegramApi, chatId, messageId)
                scholarProxy.handleFeedback(feedbackQuery!!)
            }
        }
    }


    override fun startQuiz() {
        val quizBoss = QuizBoss(telegramApi, chatId, messageId)
        quizBoss.sendIntro()
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


    override fun sendStats() {
        sendToBeDone()
    }


    private fun handleQuestion(jsonString: String) {

        this.update = gson.fromJson(jsonString, Update::class.java)
        val message = update!!.message.text

        // From information
        chatId = update!!.message.chat.id
        messageId = update!!.message.messageId

        sendTyping()

        println("Message is `$message`")

        if (message == CMD_HELP || message == CMD_START) {
            sendHelp(chatId, messageId)
        } else if (message == CMD_TEST) {
            startTest()
        } else if (message == CMD_QUIZ) {
            startQuiz()
        } else if (message == CMD_STATS) {
            sendStats()
        } else {
            val scholarProxy = ScholarProxy(
                    telegramApi,
                    chatId,
                    messageId
            )
            scholarProxy.handle(jsonString)
        }
    }

    private fun sendTyping() {
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
                            📈 $CMD_STATS To get global COVID-19 statistics
                            
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