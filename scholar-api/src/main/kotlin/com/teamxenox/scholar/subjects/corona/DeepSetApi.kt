package com.teamxenox.scholar.subjects.corona

import com.teamxenox.scholar.data.api.faqqa.AnswersRequest
import com.teamxenox.scholar.data.api.faqqa.AnswersResponse
import com.teamxenox.scholar.data.feedback.AddFeedbackRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface DeepSetApi {

    @POST("question/ask")
    fun getAnswers(@Body answersRequest: AnswersRequest): Call<AnswersResponse>

    @POST("models/{modelId}/feedback")
    fun addFeedback(@Path("modelId") modelId: String,
                    @Body addFeedbackRequest: AddFeedbackRequest): Call<Any>
}