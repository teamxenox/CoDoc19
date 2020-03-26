package com.teamxenox.telegramapi

import com.teamxenox.telegramapi.models.SendMessageRequest
import com.teamxenox.telegramapi.models.SendMessageResponse
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class Telegram(
        private val accessToken: String
) {

    companion object {
        private const val BASE_URL = "https://api.telegram.org/"
        private val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(TelegramApi::class.java)
    }


    fun sendMessage(sendMessageRequest: SendMessageRequest): SendMessageResponse? {
        val response = api.sendMessage(
                accessToken,
                sendMessageRequest
        ).execute()
        return response.body()
    }
}

