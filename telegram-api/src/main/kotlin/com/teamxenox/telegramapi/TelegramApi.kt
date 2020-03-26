package com.teamxenox.telegramapi

import com.teamxenox.telegramapi.models.SendMessageRequest
import com.teamxenox.telegramapi.models.SendMessageResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface TelegramApi {

    @POST("/bot{from}/sendMessage")
    fun sendMessage(@Path("from") from: String, @Body sendMessageRequest: SendMessageRequest) : Call<SendMessageResponse>

}