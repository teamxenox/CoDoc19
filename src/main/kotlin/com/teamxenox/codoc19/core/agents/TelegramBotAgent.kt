package com.teamxenox.codoc19.core.agents

import com.teamxenox.bootzan.GsonUtils.gson
import com.teamxenox.codoc19.core.SecretConstants
import com.teamxenox.codoc19.core.base.BotAgent
import com.teamxenox.codoc19.core.features.qa.ScholarProxy
import com.teamxenox.codoc19.core.features.quiz.QuizBoss
import com.teamxenox.codoc19.core.features.stats.CovidAnalyst
import com.teamxenox.codoc19.core.features.test.Doctor
import com.teamxenox.codoc19.data.entities.User
import com.teamxenox.codoc19.data.repos.AnalyticsRepo
import com.teamxenox.codoc19.data.repos.UserRepo
import com.teamxenox.telegramapi.Telegram
import com.teamxenox.telegramapi.models.*

open class TelegramBotAgent(
        private val userRepo: UserRepo,
        private val analyticsRepo: AnalyticsRepo
) : BotAgent(userRepo, analyticsRepo) {

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
        private val CMD_STATS_COUNTRY_REGEX = "/stats (?<country>.+)".toRegex()
    }

    override fun handle(data: Any) {

        val jsonString = gson.toJson(data)
        println("HIT!! -> $data")
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

    override fun startTest() {
        doctor = Doctor(telegramApi, chatId, messageId)
        doctor!!.startTest()
    }


    override fun sendGlobalStats() {
        val covidAnalyst = CovidAnalyst(telegramApi, chatId, messageId)
        covidAnalyst.sendGlobalStats()
    }


    private fun handleQuestion(jsonString: String) {

        println("JSON is $jsonString")
        this.update = gson.fromJson(jsonString, Update::class.java)
        val updateMessage = this.update!!.message
        if (updateMessage != null) {

            // Checking if the user exist if not add him/her to db
            val currentUser = getUser()
            println("User is ${currentUser.username}")

            val message = updateMessage.text

            println("Chat id is ${updateMessage.chat.id}")

            // From information
            chatId = updateMessage.chat.id
            messageId = updateMessage.messageId

            sendTyping()

            println("Message is `$message`")

            if (message == CMD_HELP || message == CMD_START) {
                sendHelp(chatId, messageId)
            } else if (message == CMD_TEST) {
                startTest()
            } else if (message == CMD_QUIZ) {
                startQuiz()
            } else if (message == CMD_STATS) {
                sendGlobalStats()
            } else if (CMD_STATS_COUNTRY_REGEX.matches(message)) {
                val countryName = CMD_STATS_COUNTRY_REGEX.find(message)!!.groups["country"]!!.value
                sendCountryStats(countryName)
            } else {
                val scholarProxy = ScholarProxy(
                        telegramApi,
                        chatId,
                        messageId
                )
                scholarProxy.handle(jsonString)
            }
        } else {
            println("Junk!!")
        }
    }

    private fun getUser(): User {
        val updateMsg = update!!.message!!
        val exUser = userRepo.findByUserId(updateMsg.from.id)
        return if (exUser != null) {
            println("exUser found : ${exUser.username}")
            exUser
        } else {
            val newUser = User().apply {
                userId = updateMsg.from.id
                username = updateMsg.from.username
                firstName = updateMsg.from.firstName
                platform = User.Platform.TELEGRAM
            }
            println("Creating new user : ${newUser.username}")
            userRepo.save(newUser)
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

    override fun sendCountryStats(country: String) {
        val covidAnalyst = CovidAnalyst(telegramApi, chatId, messageId)
        covidAnalyst.sendCountryStats(country)
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