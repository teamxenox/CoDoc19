package com.teamxenox.codoc19.core.features.quiz

import com.google.gson.reflect.TypeToken
import com.teamxenox.bootzan.GsonUtils
import com.teamxenox.codoc19.core.base.FeatureProxy
import com.teamxenox.codoc19.models.QuizQuestion
import com.teamxenox.telegramapi.Telegram
import com.teamxenox.telegramapi.models.SendMessageRequest

class QuizBoss(
        private val telegramApi: Telegram,
        private val chatId: Long,
        messageId: Long
) : FeatureProxy(telegramApi, chatId, messageId) {

    companion object {
        private const val CALLBACK_START_QUIZ = "startQuiz"
        private val ANSWER_REGEX = "q(?<questionId>\\d+)a(?<optionIndex>\\d+)s(?<score>\\d+)".toRegex()
        private val NEXT_REGEX = "nq(?<nextQuestionId>\\d+)s(?<score>\\d+)".toRegex()

        private val questions by lazy {
            val quizJson = QuizBoss::class.java.getResourceAsStream("/quiz.json").bufferedReader().readText()
            val type = object : TypeToken<List<QuizQuestion>>() {}.type
            GsonUtils.gson.fromJson<List<QuizQuestion>>(quizJson, type)
        }

    }

    override fun handle(@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") buttonData: String) {
        if (buttonData == CALLBACK_START_QUIZ) {
            // start quiz
            val question = questions.first()
            ask(question, 0)
        } else if (buttonData.matches(NEXT_REGEX)) {
            // clicked on next button
            val match = NEXT_REGEX.find(buttonData)!!

            val nextQuestionId = match.groups["nextQuestionId"]!!.value.toInt()
            val score = match.groups["score"]!!.value.toInt()
            val question = questions.find { it.id == nextQuestionId }!!
            ask(question, score)

        } else if (buttonData.matches(ANSWER_REGEX)) {

            // answer, parse
            val match = ANSWER_REGEX.find(buttonData)!!

            // Clicked on an option
            val questionId = match.groups["questionId"]!!.value.toInt()
            val actualAnswerIndex = match.groups["optionIndex"]!!.value.toInt()
            var score = match.groups["score"]!!.value.toInt()

            // Checking answer
            val askedQuestion = questions.find { it.id == questionId }!!
            val expectedAnswerIndex = askedQuestion.answerIndex
            val nextQuestionId = askedQuestion.id + 1
            val nextQuestion = questions.find { it.id == nextQuestionId }
            val actualAnswer = askedQuestion.options[actualAnswerIndex]
            val expectedAnswer = askedQuestion.options[expectedAnswerIndex]

            if (actualAnswerIndex == expectedAnswerIndex) {
                // correct answer clicked

                // score increment
                score += 1

                // correct answer

                sendResult("✅ Correct : $actualAnswer", askedQuestion, nextQuestion, score)

            } else {
                // wrong answer
                sendResult("❌ Wrong : $actualAnswer \n✅ Correct answer : $expectedAnswer", askedQuestion, nextQuestion, score)
            }

        } else {
            throw IllegalArgumentException("Un-managed data : `$buttonData`")
        }
    }

    private fun sendResult(
            header: String,
            askedQuestion: QuizQuestion,
            nextQuestion: QuizQuestion?,
            score: Int
    ) {
        val msg = "$header\n\n${askedQuestion.answerReason}"
        val nextButton = if (nextQuestion != null) {
            SendMessageRequest.ReplyMarkup(
                    listOf(listOf(
                            SendMessageRequest.InlineButton(
                                    "NEXT",
                                    "nq${nextQuestion.id}s$score"
                            )
                    ))
            )
        } else {
            // next question == null
            null
        }

        // send answer with next button
        telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        text = msg,
                        replyMarkup = nextButton
                )
        )

        if (nextQuestion == null) {
            // sending score
            val scoreMsg = """
                        You got $score / ${questions.size} correct 
                        
                        Keep educating yourself to protect the community and yourself.
                        Thank you for your time
                    """.trimIndent()

            telegramApi.sendMessage(
                    SendMessageRequest(
                            chatId = chatId,
                            text = scoreMsg
                    )
            )
        }
    }

    private fun ask(question: QuizQuestion, score: Int) {
        val options = mutableListOf<List<SendMessageRequest.InlineButton>>()
        for ((optionIndex, option) in question.options.withIndex()) {
            options.add(listOf(
                    SendMessageRequest.InlineButton(
                            option,
                            "q${question.id}a${optionIndex}s$score" // question 1 score 0
                    )
            ))
        }
        telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        text = "Q: ${question.question}",
                        replyMarkup = SendMessageRequest.ReplyMarkup(options)
                )
        )
    }

    fun sendIntro() {
        val message = """
            Are you taking the correct protective measures against COVID-19?
            
            Misinformation spreads faster than the virus.
            Stay informed to protect your community.
        """.trimIndent()


        telegramApi.sendMessage(
                SendMessageRequest(
                        chatId = chatId,
                        text = message,
                        replyMarkup = SendMessageRequest.ReplyMarkup(
                                listOf(
                                        listOf(
                                                SendMessageRequest.InlineButton(
                                                        "Find Out!",
                                                        CALLBACK_START_QUIZ
                                                )
                                        )
                                )
                        )
                )
        )
    }

    fun isQuizClickData(buttonData: String): Boolean {
        return buttonData == CALLBACK_START_QUIZ
                || buttonData.matches(ANSWER_REGEX)
                || buttonData.matches(NEXT_REGEX)
    }

}