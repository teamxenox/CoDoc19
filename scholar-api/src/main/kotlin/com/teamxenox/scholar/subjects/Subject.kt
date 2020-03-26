package com.teamxenox.scholar.subjects

import com.teamxenox.scholar.models.Answer

interface Subject {
    fun getAnswer(question: String): Answer?
}