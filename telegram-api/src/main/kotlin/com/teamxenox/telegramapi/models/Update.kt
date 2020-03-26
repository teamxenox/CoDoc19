package com.teamxenox.telegramapi.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Update(
        @JsonProperty("message")
        val message: Message?,
        @JsonProperty("update_id")
        val updateId: Int? // 27416780
) {
    data class Message(
            @JsonProperty("chat")
            val chat: Chat?,
            @JsonProperty("date")
            val date: Int?, // 1585251350
            @JsonProperty("entities")
            val entities: List<Entity?>?,
            @JsonProperty("from")
            val from: From?,
            @JsonProperty("message_id")
            val messageId: Long?, // 21
            @JsonProperty("text")
            val text: String? // /help
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

        data class Entity(
                @JsonProperty("length")
                val length: Int?, // 5
                @JsonProperty("offset")
                val offset: Int?, // 0
                @JsonProperty("type")
                val type: String? // bot_command
        )

        data class From(
                @JsonProperty("first_name")
                val firstName: String?, // theapache64
                @JsonProperty("id")
                val id: Int?, // 240810054
                @JsonProperty("is_bot")
                val isBot: Boolean?, // false
                @JsonProperty("language_code")
                val languageCode: String?, // en
                @JsonProperty("username")
                val username: String? // theapache64
        )
    }
}