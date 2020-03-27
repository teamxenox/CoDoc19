package com.teamxenox.telegramapi.models

import com.google.gson.annotations.SerializedName


data class SendChatActionRequest(
        @SerializedName("action")
        val action: String, // String
        @SerializedName("chat_id")
        val chatId: Long // String
)