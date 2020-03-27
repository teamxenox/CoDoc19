package com.teamxenox.telegramapi

import com.teamxenox.telegramapi.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface TelegramApi {

    @POST("/bot{from}/sendMessage")
    fun sendMessage(@Path("from") from: String, @Body sendMessageRequest: SendMessageRequest): Call<SendMessageResponse>


    @POST("/bot{from}/sendChatAction")
    fun sendChatAction(@Path("from") from: String, @Body sendMessageRequest: SendChatActionRequest): Call<SendChatActionResponse>


    @POST("/bot{from}/answerCallbackQuery")
    fun answerCallbackQuery(@Path("from") from: String, @Body answerCallbackRequest: AnswerCallbackRequest): Call<Any>
}