package com.teamxenox.scholar.subjects.corona

import com.teamxenox.scholar.data.api.faqqa.AnswersRequest
import com.teamxenox.scholar.data.api.faqqa.AnswersResponse
import com.teamxenox.scholar.data.feedback.AddFeedbackRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DeepSetApi {

    @POST("models/1/faq-qa")
    fun getAnswers(@Body answersRequest: AnswersRequest): Call<AnswersResponse>

    @POST("models/1/feedback")
    fun addFeedback(@Body addFeedbackRequest: AddFeedbackRequest): Call<Any>
}