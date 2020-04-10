package com.teamxenox.scholar.models

data class Answer(
        val modelId: String,
        val documentId: String,
        val askedQuestion: String,
        val matchedQuestion: String,
        val answer: String,
        val source: String,
        val sourceLink: String,
        val confidence: Float
)