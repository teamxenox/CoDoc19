package com.teamxenox.telegramapi.models

import com.google.gson.annotations.SerializedName


data class SendChatActionResponse(
        @SerializedName("ok")
        val ok: Boolean, // true
        @SerializedName("result")
        val result: Boolean // true
)