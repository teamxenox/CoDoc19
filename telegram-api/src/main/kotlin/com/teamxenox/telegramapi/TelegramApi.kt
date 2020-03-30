package com.teamxenox.telegramapi

import com.teamxenox.telegramapi.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface TelegramApi {

    @POST("/bot{from}/sendMessage")
    fun sendMessage(@Path("from") from: String, @Body sendMessageRequest: SendMessageRequest): Call<SendMessageResponse>

    @POST("/bot{from}/sendChatAction")
    fun sendChatAction(@Path("from") from: String, @Body sendMessageRequest: SendChatActionRequest): Call<SendChatActionResponse>

    @POST("/bot{from}/answerCallbackQuery")
    fun answerCallbackQuery(@Path("from") from: String, @Body answerCallbackRequest: AnswerCallbackRequest): Call<Any>

    @Multipart
    @POST("/bot{from}/sendPhoto")
    fun sendPhotoFile(
            @Path("from") from: String,
            @Part("chat_id") chatId: RequestBody,
            @Part photo: MultipartBody.Part
    ): Call<SendPhotoResponse>
}