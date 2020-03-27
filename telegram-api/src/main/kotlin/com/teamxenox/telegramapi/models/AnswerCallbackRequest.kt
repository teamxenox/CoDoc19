package com.teamxenox.telegramapi.models

import com.google.gson.annotations.SerializedName

data class AnswerCallbackRequest(
        @SerializedName("callback_query_id")
        val callbackQueryId: String // 123
)