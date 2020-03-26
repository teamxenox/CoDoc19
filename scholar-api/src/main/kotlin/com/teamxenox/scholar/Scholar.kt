package com.teamxenox.scholar

import com.teamxenox.scholar.models.Answer
import com.teamxenox.scholar.subjects.Subject

object Scholar {

    fun getAnswer(subject: Subject, question: String): Answer? {
        return subject.getAnswer(question)
    }

}