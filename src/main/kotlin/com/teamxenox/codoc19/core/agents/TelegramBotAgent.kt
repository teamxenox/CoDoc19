package com.teamxenox.codoc19.core.agents

import com.teamxenox.bootzan.GsonUtils
import com.teamxenox.bootzan.GsonUtils.gson
import com.teamxenox.codoc19.core.features.qa.FeedbackParser
import com.teamxenox.codoc19.core.SecretConstants
import com.teamxenox.codoc19.core.base.BotAgent
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

        private const val BUTTON_YES = "✅ YES"
        private const val BUTTON_NO = "❌ NO"

        private val TEST_ANSWER_YES_NO_DATA_REGEX = "(?<questionId>\\d+)(?<answer>[yn])".toRegex()

        private const val ID_FEAR = 1
        private const val ID_FEVER = 2
        private const val ID_COUGH = 3
        private const val ID_SOB = 4
        private const val ID_AGE_ABOVE_50 = 5

        private val questions = mapOf(
                ID_FEAR to "Do you fear that you might have COVID-19 ? 🤔",
                ID_FEVER to "Do you have fever?",
                ID_COUGH to "Do you have cough?",
                ID_SOB to "Do you feel shortness of breath?",
                ID_AGE_ABOVE_50 to "How old ar e you? Are you above 50 years old ?"
        )
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

        if (isTestButtonClick(buttonData)) {

            println("It's test button click")

            val matcher = TEST_ANSWER_YES_NO_DATA_REGEX.find(buttonData)!!
            val questionId = matcher.groups["questionId"]!!.value.toInt()
            val answer = matcher.groups["answer"]!!.value

            onAnswer(questionId, answer, buttonData)

        } else {
            println("It's feedback for scholar API")
            // question feedback for scholar API
            handleFeedback()
        }
    }

    private fun onAnswer(questionId: Int, answer: String, buttonData: String) {

        println("Question $questionId answered `$answer`")
        val data = "$questionId$answer"
        when (questionId) {
            ID_FEAR -> {

                if (answer == "y") {
                    // Asking if got fever
                    ask(ID_FEVER, null)
                } else {
                    telegramApi.sendMessage(
                            SendMessageRequest(
                                    chatId = chatId,
                                    text = "You're lucky 😇"
                            )
                    )
                }
            }

            // fever
            ID_FEVER -> {
                ask(ID_COUGH, data)
            }

            // cough
            ID_COUGH -> {
                ask(ID_SOB, buttonData)
            }

            // shortness of breath
            ID_SOB -> {
                // here, we'll have answer for fever, cough and sb
                val sb = answer
                val answers = buttonData.split("-")
                println(answer)
                val fever = answers.find { it.contains("$ID_FEVER") }!!
                        .replace("$ID_FEVER", "")
                val cough = answers.find { it.contains("$ID_COUGH") }!!
                        .replace("$ID_COUGH", "")

                if (sb == "y" && fever == "y" && cough == "y") {
                    ask(ID_AGE_ABOVE_50, null)
                }
            }

            // age
            ID_AGE_ABOVE_50 -> {
                if (answer == "y") {
                    /**
                     * - Tell them Isolate themselves
                    - Give them Local authority number
                     */
                } else {

                }
            }
        }
    }

    private fun ask(questionId: Int, data: String?) {

        val buttons = getYesNoButtons(questionId, data)
        val question = "${questions[questionId]}"

        telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        text = question,
                        replyMarkup = buttons
                )
        )
    }


    private fun isTestButtonClick(buttonData: String): Boolean {
        val answers = buttonData.split("-")
        var isAllMatch = true
        answers.forEach { answer ->
            isAllMatch = answer.matches(TEST_ANSWER_YES_NO_DATA_REGEX) && isAllMatch
        }
        return isAllMatch
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
        println("Starting test...")

        val buttons = getYesNoButtons(ID_FEAR, null)
        val question = "Okay, Let's start the test 😊 \n\n${questions[ID_FEAR]}"

        telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        text = question,
                        replyMsgId = messageId,
                        replyMarkup = buttons
                )
        )
    }

    private fun getYesNoButtons(questionId: Int, data: String?): SendMessageRequest.ReplyMarkup? {
        val prevData = if (data != null) {
            "-$data"
        } else {
            ""
        }
        return SendMessageRequest.ReplyMarkup(
                listOf(
                        listOf(
                                SendMessageRequest.InlineButton(
                                        BUTTON_YES,
                                        "${questionId}y$prevData"
                                ),
                                SendMessageRequest.InlineButton(
                                        BUTTON_NO,
                                        "${questionId}n$prevData"
                                )
                        )
                )
        )
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
        this.feedbackQuery = GsonUtils.gson.fromJson(jsonString, CallbackQueryResponse::class.java)
        return this.feedbackQuery?.callbackQuery != null
    }


}