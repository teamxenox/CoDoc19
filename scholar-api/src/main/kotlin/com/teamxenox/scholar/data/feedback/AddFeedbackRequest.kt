package com.teamxenox.scholar.data.feedback

import com.google.gson.annotations.SerializedName


data class AddFeedbackRequest(
        @SerializedName("feedback")
        val feedback: String,  // relevant
        @SerializedName("question")
        val question: String,
        @SerializedName("document_id")
        val documentId: Long
)