package com.teamxenox.telegramapi.models

import com.fasterxml.jackson.annotation.JsonProperty


data class SendMessageResponse(
        @JsonProperty("ok")
        val ok: Boolean?, // true
        @JsonProperty("result")
        val result: Result?
) {
    data class Result(
            @JsonProperty("chat")
            val chat: Chat?,
            @JsonProperty("date")
            val date: Int?, // 1585252533
            @JsonProperty("from")
            val from: From?,
            @JsonProperty("message_id")
            val messageId: Int?, // 84
            @JsonProperty("text")
            val text: String? // Hey
    ) {
        data class Chat(
                @JsonProperty("first_name")
                val firstName: String?, // theapache64
                @JsonProperty("id")
                val id: Int?, // 240810054
                @JsonProperty("type")
                val type: String?, // private
                @JsonProperty("username")
                val username: String? // theapache64
        )

        data class From(
                @JsonProperty("first_name")
                val firstName: String?, // CoDoc19Dev
                @JsonProperty("id")
                val id: Int?, // 1028883028
                @JsonProperty("is_bot")
                val isBot: Boolean?, // true
                @JsonProperty("username")
                val username: String? // CoDoc19DevBot
        )
    }
}