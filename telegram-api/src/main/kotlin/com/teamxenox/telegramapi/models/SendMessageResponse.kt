package com.teamxenox.telegramapi.models

import com.google.gson.annotations.SerializedName


data class SendMessageResponse(
        @SerializedName("ok")
        val ok: Boolean?, // true
        @SerializedName("result")
        val result: Result?
) {
    data class Result(
            @SerializedName("chat")
            val chat: Chat?,
            @SerializedName("date")
            val date: Int?, // 1585252533
            @SerializedName("from")
            val from: From?,
            @SerializedName("message_id")
            val messageId: Int?, // 84
            @SerializedName("text")
            val text: String? // Hey
    ) {
        data class Chat(
                @SerializedName("first_name")
                val firstName: String?, // theapache64
                @SerializedName("id")
                val id: Int?, // 240810054
                @SerializedName("type")
                val type: String?, // private
                @SerializedName("username")
                val username: String? // theapache64
        )

        data class From(
                @SerializedName("first_name")
                val firstName: String?, // CoDoc19Dev
                @SerializedName("id")
                val id: Int?, // 1028883028
                @SerializedName("is_bot")
                val isBot: Boolean?, // true
                @SerializedName("username")
                val username: String? // CoDoc19DevBot
        )
    }
}