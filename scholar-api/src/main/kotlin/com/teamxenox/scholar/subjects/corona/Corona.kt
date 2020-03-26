package com.teamxenox.scholar.subjects.corona

import com.teamxenox.scholar.models.Answer
import com.teamxenox.scholar.subjects.Subject
import com.teamxenox.scholar.data.api.faqqa.AnswersRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Corona : Subject {

    private const val BASE_URL = "https://covid-backend.deepset.ai/"
    private val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DeepSetApi::class.java)

    /**
     * To get answer for the question
     */
    override fun getAnswer(question: String): Answer? {

        val answersResponse = api.getAnswers(
                AnswersRequest(
                        arrayOf(question)
                )
        ).execute().body()

        if (answersResponse?.results!!.first().answers.isNotEmpty()) {
            val ans = answersResponse.results.first().answers.first()
            return Answer(
                    ans.meta.documentId,
                    ans.question,
                    ans.answer,
                    ans.meta.source,
                    (ans.probability * 100).toFloat()
            )
        }

        return null
    }

}