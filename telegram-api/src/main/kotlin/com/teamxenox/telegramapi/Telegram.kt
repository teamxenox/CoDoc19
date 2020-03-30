package com.teamxenox.telegramapi

import com.teamxenox.telegramapi.models.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class Telegram(
        private val accessToken: String
) {

    companion object {
        private const val BASE_URL = "https://api.telegram.org/"

        // Chat actions
        const val CHAT_ACTION_TYPING = "typing"


        private val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpClient.Builder().apply {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY

                    //addInterceptor(logging)
                }.build())
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

    fun sendPhotoFile(chatId: Long, file: File): Response<SendPhotoResponse> {
        val mediaType = MediaType.parse("multipart/form-data")
        val requestFile = RequestBody.create(mediaType, file)
        val photoPart = MultipartBody.Part.createFormData("photo", file.name, requestFile)
        val chatIdPart = RequestBody.create(mediaType, chatId.toString())
        return api.sendPhotoFile(
                accessToken,
                chatIdPart,
                photoPart
        ).execute()
    }

    fun sendPhoto(request: SendPhotoRequest): Response<SendPhotoResponse> {
        return api.sendPhoto(
                accessToken,
                request
        ).execute()
    }
}

