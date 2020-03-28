package com.teamxenox.codoc19.core.features.test

import com.teamxenox.codoc19.core.ContactManager
import com.teamxenox.codoc19.core.base.FeatureProxy
import com.teamxenox.telegramapi.Telegram
import com.teamxenox.telegramapi.models.SendMessageRequest

class Doctor(
        private val telegramApi: Telegram,
        private val chatId: Long,
        private val messageId: Long
) : FeatureProxy(telegramApi, chatId, messageId) {

    companion object {
        private const val BUTTON_YES = "✅ YES"
        private const val BUTTON_NO = "❌ NO"

        private val TEST_ANSWER_YES_NO_DATA_REGEX = "(?<questionId>\\d+)(?<answer>[yn])".toRegex()
        private val TEST_ANSWER_LOCATION = "s(?<state>.+)".toRegex()

        private const val ID_FEAR = 1
        private const val ID_FEVER = 2
        private const val ID_COUGH = 3
        private const val ID_SOB = 4
        private const val ID_AGE_ABOVE_50 = 5
        private const val ID_LOCATION = 6
        private const val ID_CONDITION = 7
        private const val ID_RUNNY_NOSE = 8
        private const val ID_MUSCLE_ACHES = 9
        private const val ID_FATIGUE = 10

        private val questions = mapOf(

                ID_FEAR to "Do you fear that you might have COVID-19 ? 🤔",

                // sequential questions for covid-19
                ID_FEVER to "Do you have fever?",
                ID_COUGH to "Do you have cough?",
                ID_SOB to "Do you feel shortness of breath?",

                ID_AGE_ABOVE_50 to "How old ar e you? Are you above 50 years old ?",
                ID_LOCATION to "Where do you live? 🌎",
                ID_CONDITION to "Is your condition is really bad? 😷",

                // sequential questions for common flu
                ID_RUNNY_NOSE to "Do you have runny nose? 👃",
                ID_MUSCLE_ACHES to "Do you have muscle aches?",
                ID_FATIGUE to "Do you feel fatigue?"
        )

    }

    /**
     * To data button click
     */
    override fun handle(@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") buttonData: String) {

        val yesOrNo = TEST_ANSWER_YES_NO_DATA_REGEX.find(buttonData)

        if (yesOrNo != null) {
            val questionId = yesOrNo.groups["questionId"]!!.value.toInt()
            val answer = yesOrNo.groups["answer"]!!.value

            onAnswer(questionId, answer, buttonData)

        } else {
            // it's location data, so parse it
            val locationResult = TEST_ANSWER_LOCATION.find(buttonData)!!
            val stateName = locationResult.groups["state"]!!.value
            onAnswer(ID_LOCATION, stateName, buttonData)
        }
    }

    /**
     * Called when answer is parsed from button click
     */
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
                @Suppress("UnnecessaryVariable")
                val sb = answer

                val answers = buttonData.split("-")
                val fever = answers.find { it.contains("$ID_FEVER") }!!
                        .replace("$ID_FEVER", "")
                val cough = answers.find { it.contains("$ID_COUGH") }!!
                        .replace("$ID_COUGH", "")

                if (sb == "y" && fever == "y" && cough == "y") {
                    ask(ID_AGE_ABOVE_50, null)
                } else if (sb == "y" || fever == "y" || cough == "y") {
                    // few yes. Ask them question about common symptoms of common or seasonal flu . RN, MA, F
                    ask(ID_RUNNY_NOSE, null)
                } else {
                    // no symptoms . you're okay
                    sendYouAreOkay()
                }
            }

            // age
            ID_AGE_ABOVE_50 -> {
                if (answer == "y") {
                    ask(ID_LOCATION, null)
                } else {
                    ask(ID_CONDITION, null)
                }
            }

            // location
            ID_LOCATION -> {

                val phoneNumber = ContactManager.contacts.find { it.state == answer } ?: ContactManager.WHO_HELPLINE

                val message = """
                    Please isolate yourself. Here's your authority's number 
                    📞 ${phoneNumber.state}: [${phoneNumber.number}](tel:${phoneNumber.number})
                """.trimIndent()

                telegramApi.sendMessage(
                        SendMessageRequest(
                                chatId = chatId,
                                text = message,
                                parseMode = "Markdown"
                        )
                )
            }

            ID_CONDITION -> {

                if (answer == "y") {
                    // condition is bad
                    ask(ID_LOCATION, null)
                } else {
                    // condition not bad
                    telegramApi.sendMessage(
                            SendMessageRequest(chatId = chatId, text = "Please isolate yourself and stay away from elderly people 👨‍🦳. We'll fight this")
                    )
                }

            }

            // runny nose
            ID_RUNNY_NOSE -> {
                ask(ID_MUSCLE_ACHES, data)
            }

            // muscle ache
            ID_MUSCLE_ACHES -> {
                ask(ID_FATIGUE, buttonData)
            }

            // runny nose, muscle ache and fatigue
            ID_FATIGUE -> {

                @Suppress("DuplicatedCode")
                val fatigue = answer
                val answers = buttonData.split("-")

                val runnyNose = answers.find { it.contains("$ID_RUNNY_NOSE") }!!
                        .replace("$ID_RUNNY_NOSE", "")
                val muscleAches = answers.find { it.contains("$ID_MUSCLE_ACHES") }!!
                        .replace("$ID_MUSCLE_ACHES", "")


                if (fatigue == "y" && runnyNose == "y" && muscleAches == "y") {
                    // tell them that they common flu or seasonal flu
                    telegramApi.sendMessage(
                            SendMessageRequest(
                                    chatId = chatId,
                                    text = "Don't worry. You're just having common flu"
                            )
                    )
                } else {
                    // tell them they have nothing
                    sendYouAreOkay()
                }

            }
        }
    }

    private fun sendYouAreOkay() {
        telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        text = "Don't worry. You've nothing 🙂"
                )
        )
    }

    /**
     * To ask a new question. data passed will be chained along with the question.
     * Useful for sequential questioning
     */
    private fun ask(questionId: Int, data: String?) {

        if (questionId == ID_LOCATION) {
            // asking users location
            val locationButtons = getLocationButtons()
            telegramApi.sendMessage(
                    SendMessageRequest(
                            chatId = chatId,
                            text = "Select your state 📌",
                            replyMarkup = locationButtons
                    )
            )

        } else {
            // yes or no question
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


    }

    /**
     * To get states list as Reply Buttons
     */
    private fun getLocationButtons(): SendMessageRequest.ReplyMarkup {

        val buttons = mutableListOf<List<SendMessageRequest.InlineButton>>()
        val states = ContactManager.getStates().toMutableList()
        states.add("🗺️ Other")

        for (state in states.chunked(2)) {

            if (state.size == 2) {
                buttons.add(listOf(
                        SendMessageRequest.InlineButton(state[0], "s${state[0]}"),
                        SendMessageRequest.InlineButton(state[1], "s${state[1]}")
                ))
            } else {
                buttons.add(listOf(
                        SendMessageRequest.InlineButton(state[0], "s${state[0]}")
                ))
            }
        }



        return SendMessageRequest.ReplyMarkup(
                buttons
        )
    }

    /**
     * To get YES or NO buttons
     */
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

    /**
     * To start a test
     */
    fun startTest() {

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


    /**
     * To check if it's a test button click
     */
    fun isTestButtonClick(buttonData: String): Boolean {
        // Checking if it's location data
        return if (TEST_ANSWER_LOCATION.matches(buttonData)) {
            true
        } else {
            val answers = buttonData.split("-")
            var isAllMatch = true
            answers.forEach { answer ->
                isAllMatch = answer.matches(TEST_ANSWER_YES_NO_DATA_REGEX) && isAllMatch
            }
            isAllMatch
        }
    }

}