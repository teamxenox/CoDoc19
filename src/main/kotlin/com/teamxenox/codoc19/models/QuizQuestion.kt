package com.teamxenox.codoc19.models

import com.google.gson.annotations.SerializedName


data class QuizQuestion(
        @SerializedName("question")
        val question: String, // At least how long should you wash your hands for?
        @SerializedName("options")
        val options: List<String>,
        @SerializedName("answer_index")
        val answerIndex: Int, // 2
        @SerializedName("reason")
        val reason: String // Hand sanitizer with over 60% alcohol does kill bacteria, but it does not replace washing your hands. Use hand sanitizer when you don't have access to washing your hands with soap and water.
)