package com.teamxenox.scholar.data.api.faqqa

import com.google.gson.annotations.SerializedName


class AnswersRequest(
        @SerializedName("questions")
        val questions: Array<String>, // How does corona spread?
        @SerializedName("top_k_retriever")
        val resultCount: Int = 1
)