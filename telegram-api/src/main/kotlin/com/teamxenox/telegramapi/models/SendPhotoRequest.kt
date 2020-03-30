package com.teamxenox.telegramapi.models

import com.google.gson.annotations.SerializedName


data class SendPhotoRequest(
        @SerializedName("chat_id")
        val chatId: Long, // 24234234234
        @SerializedName("photo")
        val photo: String // 23423423423423
)