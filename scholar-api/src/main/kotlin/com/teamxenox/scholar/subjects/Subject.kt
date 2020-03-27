package com.teamxenox.scholar.subjects

import com.teamxenox.scholar.models.Answer
import com.teamxenox.scholar.models.Feedback

interface Subject {
    fun getAnswer(question: String): Answer?
    fun addFeedback(feedback: Feedback): Boolean
}