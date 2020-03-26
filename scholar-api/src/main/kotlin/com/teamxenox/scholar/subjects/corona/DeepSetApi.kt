package com.teamxenox.scholar.subjects.corona

import com.teamxenox.scholar.data.api.faqqa.AnswersRequest
import com.teamxenox.scholar.data.api.faqqa.AnswersResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DeepSetApi {

    @POST("models/1/faq-qa")
    fun getAnswers(@Body answersRequest: AnswersRequest): Call<AnswersResponse>
}