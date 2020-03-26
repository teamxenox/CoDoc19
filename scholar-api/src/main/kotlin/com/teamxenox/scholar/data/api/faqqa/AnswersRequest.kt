package com.teamxenox.scholar.data.api.faqqa

import com.fasterxml.jackson.annotation.JsonProperty

class AnswersRequest(
        @JsonProperty("questions")
        val questions: Array<String>, // How does corona spread?
        @JsonProperty("top_k_retriever")
        val resultCount: Int = 1
)