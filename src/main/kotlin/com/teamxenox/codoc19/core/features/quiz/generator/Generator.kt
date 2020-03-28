package com.teamxenox.codoc19.core.features.quiz.generator

import com.google.gson.Gson
import com.teamxenox.codoc19.models.QuizQuestion
import java.io.File

/**
 * DO NOT RUN THIS FILE. ASK @theapache64 why!
 */
fun main() {
    val quizSourceFile = File("/home/theapache64/Documents/projects/codoc19/extras/quiz_source.json")
    val gson = Gson()
    val quizSrc = gson.fromJson(quizSourceFile.readText(), QuizSource::class.java)
    val questions = mutableListOf<QuizQuestion>()
    for ((id, page) in quizSrc.pages.withIndex()) {

        val question = page.contentComponents[0].text
        val options = page.contentComponents[0].answers.map {
            it.text
        }
        val answer = page.contentComponents[0].answers.find { it.correct }!!.text
        val answerIndex = options.indexOf(answer)
        val feedbackText = page.contentComponents[0].feedbackText

        questions.add(
                QuizQuestion(
                        id + 1,
                        question,
                        options,
                        answerIndex,
                        feedbackText
                )
        )

        File("assets/quiz_new.json").writeText(gson.toJson(questions))

    }
}