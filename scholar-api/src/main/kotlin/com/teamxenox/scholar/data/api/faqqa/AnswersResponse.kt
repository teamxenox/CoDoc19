package com.teamxenox.scholar.data.api.faqqa

import com.google.gson.annotations.SerializedName

class AnswersResponse(
        @SerializedName("results")
        val results: List<Result>
)

data class Result(
        @SerializedName("answers")
        val answers: List<Answer>,
        @SerializedName("question")
        val question: String // How does corona spread?
) {
    data class Answer(
            @SerializedName("answer")
            val answer: String,
            @SerializedName("context")
            val context: String,
            @SerializedName("meta")
            val meta: Meta,
            @SerializedName("offset_end")
            val offsetEnd: Int, // 642
            @SerializedName("offset_start")
            val offsetStart: Int, // 0
            @SerializedName("probability")
            val probability: Double, // 0.54761047
            @SerializedName("question")
            val question: String, // How long does the virus survive on surfaces?
            @SerializedName("score")
            val score: Double // 5.4761047
    ) {
        data class Meta(
                @SerializedName("answer_html")
                val answerHtml: String,
                @SerializedName("category")
                val category: String,
                @SerializedName("city")
                val city: String,
                @SerializedName("country")
                val country: String,
                @SerializedName("document_id")
                val documentId: String, // 131
                @SerializedName("document_name")
                val documentName: String, // Q&A on coronaviruses (COVID-19)
                @SerializedName("lang")
                val lang: String, // en
                @SerializedName("last_update")
                val lastUpdate: String, // 2020/03/17
                @SerializedName("link")
                val link: String, // https://www.who.int/news-room/q-a-detail/q-a-coronaviruses
                @SerializedName("paragraph_id")
                val paragraphId: String, // zv7x7XABvTaZvFwu2OJO
                @SerializedName("question")
                val question: String, // How long does the virus survive on surfaces?
                @SerializedName("region")
                val region: String,
                @SerializedName("score")
                val score: String, // 5.4761047
                @SerializedName("source")
                val source: String // World Health Organization (WHO)
        )
    }
}