package com.teamxenox.scholar.subjects.corona

import com.google.gson.Gson
import com.teamxenox.scholar.data.api.faqqa.AnswersRequest
import com.teamxenox.scholar.data.feedback.AddFeedbackRequest
import com.teamxenox.scholar.models.Answer
import com.teamxenox.scholar.models.Feedback
import com.teamxenox.scholar.subjects.Subject
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
            val result = answersResponse.results.first()
            val ans = result.answers.first()
            return Answer(
                    result.modelId.toString(),
                    ans.meta.documentId,
                    question,
                    ans.question,
                    ans.answer,
                    ans.meta.source,
                    ans.meta.link,
                    (ans.probability * 100).toFloat()
            )
        }

        return null
    }

    override fun addFeedback(feedback: Feedback): Boolean {
        val response = api.addFeedback(
                feedback.modelId,
                AddFeedbackRequest(
                        feedback.feedback,
                        feedback.question,
                        feedback.documentId
                )
        ).execute()

        val isSuccess = response.code() == 200
        if (isSuccess) {
            println("Feedback sent")
        } else {
            println("Failed to send feedback : ${Gson().toJson(response.body())}")
        }
        return isSuccess
    }

}