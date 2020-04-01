package com.teamxenox.telegramapi

import com.teamxenox.telegramapi.models.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class Telegram(
        private val accessToken: String
) {

    companion object {
        private const val BASE_URL = "https://api.telegram.org/"

        // Chat actions
        const val CHAT_ACTION_TYPING = "typing"
        const val CHAT_ACTION_SENDING_PHOTO = "upload_photo"


        private val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .followRedirects(true)
                        .followSslRedirects(true)
                        /*.apply {
                            val logging = HttpLoggingInterceptor()
                            logging.level = HttpLoggingInterceptor.Level.BODY
                            addInterceptor(logging)
                        }*/.build())
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

    fun sendChatActionAsync(request: SendChatActionRequest): Call<SendChatActionResponse> {
        return api.sendChatAction(
                accessToken,
                request
        )
    }

    fun answerCallbackQuery(request: AnswerCallbackRequest): Response<Any> {
        return api.answerCallbackQuery(
                accessToken,
                request
        ).execute()
    }

    fun sendPhotoFile(chatId: Long, file: File, caption: String): Response<SendPhotoResponse> {
        val mediaType = MediaType.parse("multipart/form-data")
        val requestFile = RequestBody.create(mediaType, file)
        val photoPart = MultipartBody.Part.createFormData("photo", file.name, requestFile)
        val chatIdPart = RequestBody.create(mediaType, chatId.toString())
        val captionPart = RequestBody.create(mediaType, caption)
        return api.sendPhotoFile(
                accessToken,
                chatIdPart,
                captionPart,
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

