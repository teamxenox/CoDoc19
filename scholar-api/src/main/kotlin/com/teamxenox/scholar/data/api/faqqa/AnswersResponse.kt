package com.teamxenox.scholar.data.api.faqqa

import com.fasterxml.jackson.annotation.JsonProperty

class AnswersResponse(
        @JsonProperty("results")
        val results: List<Result>
)

data class Result(
        @JsonProperty("answers")
        val answers: List<Answer>,
        @JsonProperty("question")
        val question: String // How does corona spread?
) {
    data class Answer(
            @JsonProperty("answer")
            val answer: String,
            @JsonProperty("context")
            val context: String,
            @JsonProperty("meta")
            val meta: Meta,
            @JsonProperty("offset_end")
            val offsetEnd: Int, // 642
            @JsonProperty("offset_start")
            val offsetStart: Int, // 0
            @JsonProperty("probability")
            val probability: Double, // 0.54761047
            @JsonProperty("question")
            val question: String, // How long does the virus survive on surfaces?
            @JsonProperty("score")
            val score: Double // 5.4761047
    ) {
        data class Meta(
                @JsonProperty("answer_html")
                val answerHtml: String,
                @JsonProperty("category")
                val category: String,
                @JsonProperty("city")
                val city: String,
                @JsonProperty("country")
                val country: String,
                @JsonProperty("document_id")
                val documentId: String, // 131
                @JsonProperty("document_name")
                val documentName: String, // Q&A on coronaviruses (COVID-19)
                @JsonProperty("lang")
                val lang: String, // en
                @JsonProperty("last_update")
                val lastUpdate: String, // 2020/03/17
                @JsonProperty("link")
                val link: String, // https://www.who.int/news-room/q-a-detail/q-a-coronaviruses
                @JsonProperty("paragraph_id")
                val paragraphId: String, // zv7x7XABvTaZvFwu2OJO
                @JsonProperty("question")
                val question: String, // How long does the virus survive on surfaces?
                @JsonProperty("region")
                val region: String,
                @JsonProperty("score")
                val score: String, // 5.4761047
                @JsonProperty("source")
                val source: String // World Health Organization (WHO)
        )
    }
}