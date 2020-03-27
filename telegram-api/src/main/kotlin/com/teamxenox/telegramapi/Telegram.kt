package com.teamxenox.telegramapi

import com.teamxenox.telegramapi.models.SendMessageRequest
import com.teamxenox.telegramapi.models.SendMessageResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Telegram(
        private val accessToken: String
) {

    companion object {
        private const val BASE_URL = "https://api.telegram.org/"

        private val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TelegramApi::class.java)
    }


    fun sendMessage(sendMessageRequest: SendMessageRequest): Response<SendMessageResponse> {
        return api.sendMessage(
                accessToken,
                sendMessageRequest
        ).execute()
    }
}

