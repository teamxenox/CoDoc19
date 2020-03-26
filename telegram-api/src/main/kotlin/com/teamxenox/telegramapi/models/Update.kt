package com.teamxenox.telegramapi.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Update(
        @JsonProperty("message")
        val message: Message,
        @JsonProperty("update_id")
        val updateId: Int // 102073005
) {
    data class Message(
            @JsonProperty("chat")
            val chat: Chat,
            @JsonProperty("date")
            val date: Int, // 1584880886
            @JsonProperty("from")
            val from: From,
            @JsonProperty("message_id")
            val messageId: Long, // 8
            @JsonProperty("text")
            val text: String // Dbrhrfjggkjgj nfgntnt t
    ) {
        data class Chat(
                @JsonProperty("first_name")
                val firstName: String, // theapache64
                @JsonProperty("id")
                val id: Int, // 240810054
                @JsonProperty("type")
                val type: String, // private
                @JsonProperty("username")
                val username: String // theapache64
        )

        data class From(
                @JsonProperty("first_name")
                val firstName: String, // theapache64
                @JsonProperty("id")
                val id: Int, // 240810054
                @JsonProperty("is_bot")
                val isBot: Boolean, // false
                @JsonProperty("language_code")
                val languageCode: String, // en
                @JsonProperty("username")
                val username: String // theapache64
        )
    }
}