package com.teamxenox.codoc19.core.agents

import com.teamxenox.bootzan.GsonUtils.gson
import com.teamxenox.codoc19.core.geography.Geographer
import com.teamxenox.codoc19.core.SecretConstants
import com.teamxenox.codoc19.core.base.BotAgent
import com.teamxenox.codoc19.core.features.qa.ScholarProxy
import com.teamxenox.codoc19.core.features.quiz.QuizBoss
import com.teamxenox.codoc19.core.features.stats.CovidAnalyst
import com.teamxenox.codoc19.core.features.test.Doctor
import com.teamxenox.codoc19.data.entities.Analytics
import com.teamxenox.codoc19.data.entities.User
import com.teamxenox.codoc19.data.repos.AnalyticsRepo
import com.teamxenox.codoc19.data.repos.ChartRepo
import com.teamxenox.codoc19.data.repos.UserRepo
import com.teamxenox.codoc19.models.Country
import com.teamxenox.covid19api.core.JHUCSVParser
import com.teamxenox.telegramapi.Telegram
import com.teamxenox.telegramapi.models.*
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

open class TelegramBotAgent(
        private val userRepo: UserRepo,
        private val analyticsRepo: AnalyticsRepo,
        private val chartRepo: ChartRepo
) : BotAgent(userRepo, analyticsRepo) {

    private lateinit var currentUser: User
    private var update: Update? = null
    private var feedbackQuery: CallbackQueryResponse? = null
    private val telegramApi = Telegram(SecretConstants.TELEGRAM_ACTIVE_BOT_TOKEN)
    private var chatId: Long = -1
    private var messageId: Long = -1
    private var doctor: Doctor? = null
    private var covidAnalyst: CovidAnalyst? = null
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



        try {
            this.currentUser = getUserFromUpdate(
                    query.from.id,
                    query.from.username,
                    query.from.firstName
            )

            doctor = Doctor(telegramApi, chatId, messageId)
            quizBoss = QuizBoss(telegramApi, chatId, messageId)
            covidAnalyst = CovidAnalyst(telegramApi, chatId, messageId)

            when {
                doctor!!.isTestButtonClick(buttonData) -> {
                    println("It's test button click")
                    sendTyping()
                    doctor!!.handle(buttonData)

                }

                covidAnalyst!!.isChartRequest(buttonData) -> {
                    println("It's a chart request! $buttonData")
                    covidAnalyst!!.sendChart(currentUser, buttonData, chartRepo)

                    // Adding to analytics
                    analyticsRepo.save(Analytics().apply {
                        userId = currentUser.id
                        feature = Analytics.Feature.STATS_CHART
                        data = buttonData
                    })
                }

                quizBoss!!.isQuizClickData(buttonData) -> {
                    println("It's a quiz click")
                    sendTyping()
                    quizBoss!!.handle(buttonData)
                }

                else -> {
                    println("It's feedback for scholar API")
                    // question feedback for scholar API
                    sendTyping()
                    val scholarProxy = ScholarProxy(telegramApi, chatId, messageId)
                    scholarProxy.handleFeedback(feedbackQuery!!)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            sendUnknownErrorMessage()
        }
    }


    override fun startQuiz() {
        this.quizBoss = QuizBoss(telegramApi, chatId, messageId)
        quizBoss!!.sendIntro()

        // Adding to analytics
        analyticsRepo.save(Analytics().apply {
            userId = currentUser.id
            userId = currentUser.id
            feature = Analytics.Feature.QUIZ
        })
    }

    override fun startTest() {
        doctor = Doctor(telegramApi, chatId, messageId)
        doctor!!.startTest()

        // Adding to analytics
        analyticsRepo.save(Analytics().apply {
            userId = currentUser.id
            feature = Analytics.Feature.TEST
        })
    }


    override fun sendGlobalStats() {
        this.covidAnalyst = CovidAnalyst(telegramApi, chatId, messageId)
        covidAnalyst!!.sendGlobalStats()

        // Adding to analytics
        analyticsRepo.save(Analytics().apply {
            userId = currentUser.id
            feature = Analytics.Feature.STATS
            data = JHUCSVParser.COUNTRY_GLOBAL
        })
    }


    private fun handleQuestion(jsonString: String) {

        this.update = gson.fromJson(jsonString, Update::class.java)
        val updateMessage = this.update!!.message
        if (updateMessage != null) {


            // Checking if the user exist if not add him/her to db
            this.currentUser = getUserFromUpdate(
                    updateMessage.from.id,
                    updateMessage.from.username,
                    updateMessage.from.firstName
            )
            println("User is ${currentUser.username}")

            val message = updateMessage.text

            println("Chat id is ${updateMessage.chat.id}")

            // From information
            chatId = updateMessage.chat.id
            messageId = updateMessage.messageId

            try {
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
                    val country = Geographer.getCountry(countryName)
                    if (country != null) {
                        sendCountryStats(country)
                    } else {
                        telegramApi.sendMessage(
                                SendMessageRequest(
                                        chatId = chatId,
                                        replyMsgId = messageId,
                                        text = "Seems like $countryName is not a country at all 🤕"
                                )
                        )
                    }
                } else {

                    // checking if it's a country name
                    val country = Geographer.getCountry(message)
                    if (country != null) {
                        //it's a country name
                        sendCountryStats(country)
                    } else {
                        val scholarProxy = ScholarProxy(
                                telegramApi,
                                chatId,
                                messageId
                        )

                        analyticsRepo.save(Analytics().apply {
                            userId = currentUser.id
                            feature = Analytics.Feature.QA
                            data = message
                        })

                        scholarProxy.handle(jsonString)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                sendUnknownErrorMessage()
            }
        } else {
            println("Junk!!")
        }
    }

    private fun getUserFromUpdate(
            tgUserId: Int,
            tgUsername: String,
            tgFirstName: String
    ): User {
        val exUser = userRepo.findByUserId(tgUserId)
        return if (exUser != null) {
            println("exUser found : ${exUser.username}")
            exUser
        } else {
            val newUser = User().apply {
                userId = tgUserId
                username = tgUsername
                firstName = tgFirstName
                platform = User.Platform.TELEGRAM
            }
            println("Creating new user : ${newUser.username}")
            userRepo.save(newUser)
        }
    }


    private fun sendTyping() {
        sendAction(Telegram.CHAT_ACTION_TYPING)
    }


    private fun sendAction(action: String) {
        telegramApi.sendChatActionAsync(
                SendChatActionRequest(action, chatId)
        ).enqueue(object : retrofit2.Callback<SendChatActionResponse> {

            override fun onFailure(call: Call<SendChatActionResponse>, t: Throwable) {
                println("Failed to send typing")
            }

            override fun onResponse(call: Call<SendChatActionResponse>, response: Response<SendChatActionResponse>) {
                if (response.code() == 200) {
                    println("Typing sent...")
                } else {
                    println("Failed to send typing...")
                }
            }

        })
    }

    private fun sendUnknownErrorMessage() {
        this.telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        replyMsgId = messageId,
                        text = "What? 🧐 . I am sorry, I dont' know about that. 🤷‍♂"
                )
        )
    }

    override fun sendCountryStats(country: Country) {
        val covidAnalyst = CovidAnalyst(telegramApi, chatId, messageId)
        covidAnalyst.sendCountryStats(country.name)

        analyticsRepo.save(Analytics().apply {
            userId = currentUser.id
            feature = Analytics.Feature.STATS
            data = country.name
        })
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