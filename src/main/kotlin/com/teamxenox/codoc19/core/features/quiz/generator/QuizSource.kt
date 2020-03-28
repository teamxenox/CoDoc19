package com.teamxenox.codoc19.core.features.quiz.generator

import com.google.gson.annotations.SerializedName

data class QuizSource(
        @SerializedName("pages")
        val pages: List<Page>
) {
    data class Page(
            @SerializedName("contentComponents")
            val contentComponents: List<ContentComponent>
    ) {
        data class ContentComponent(
                @SerializedName("answers")
                val answers: List<Answer>,
                @SerializedName("feedbackText")
                val feedbackText: String, // If you have COVID-19 and go to the hospital, you will infect others. If you don't, you can get infected. Call your doctor instead.
                @SerializedName("text")
                val text: String
        ) {
            data class Answer(
                    @SerializedName("correct")
                    val correct: Boolean,
                    @SerializedName("text")
                    val text: String // False
            )


        }

    }
}