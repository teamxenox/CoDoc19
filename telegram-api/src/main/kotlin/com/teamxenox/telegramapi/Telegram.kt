package com.teamxenox.telegramapi

import com.teamxenox.telegramapi.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Telegram(
        private val accessToken: String
) {

    companion object {
        private const val BASE_URL = "https://api.telegram.org/"

        // Chat actions
        const val CHAT_ACTION_TYPING = "typing"

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

    fun sendChatAction(request: SendChatActionRequest): Response<SendChatActionResponse> {
        return api.sendChatAction(
                accessToken,
                request
        ).execute()
    }

    fun answerCallbackQuery(request: AnswerCallbackRequest): Response<Any> {
        return api.answerCallbackQuery(
                accessToken,
                request
        ).execute()
    }
}

