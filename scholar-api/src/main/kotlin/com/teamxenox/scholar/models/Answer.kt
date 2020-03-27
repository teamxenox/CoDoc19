package com.teamxenox.scholar.models

data class Answer(
        val documentId: String,
        val question: String,
        val answer: String,
        val source : String,
        val sourceLink : String,
        val confidence: Float
)