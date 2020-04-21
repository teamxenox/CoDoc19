package com.teamxenox.telegramapi.models

import com.google.gson.annotations.SerializedName


data class Update(
        @SerializedName("message")
        val message: Message?,
        @SerializedName("update_id")
        val updateId: String // 27416780
) {
    data class Message(
            @SerializedName("chat")
            val chat: Chat,
            @SerializedName("date")
            val date: Int, // 1585251350
            @SerializedName("entities")
            val entities: List<Entity>,
            @SerializedName("from")
            val from: From,
            @SerializedName("message_id")
            val messageId: Long, // 21
            @SerializedName("text")
            val text: String, // /help
            @SerializedName("reply_to_message")
            val replyToMessage: ReplyToMessage?
    ) {
        data class ReplyToMessage(
                @SerializedName("chat")
                val chat: Chat,
                @SerializedName("date")
                val date: Long, // 1586515142
                @SerializedName("from")
                val from: From,
                @SerializedName("message_id")
                val messageId: Long, // 45
                @SerializedName("text")
                val text: String // Please reply to this message with your group name
        ) {
            data class Chat(
                    @SerializedName("first_name")
                    val firstName: String, // theapache64
                    @SerializedName("id")
                    val id: Long, // 240810054
                    @SerializedName("type")
                    val type: String, // private
                    @SerializedName("username")
                    val username: String // theapache64
            )

            data class From(
                    @SerializedName("first_name")
                    val firstName: String, // Flare Bot
                    @SerializedName("id")
                    val id: Int, // 1245144967
                    @SerializedName("is_bot")
                    val isBot: Boolean, // true
                    @SerializedName("username")
                    val username: String // FlareAndroidBot
            )
        }

        data class Chat(
                @SerializedName("first_name")
                val firstName: String, // theapache64
                @SerializedName("id")
                val id: Long, // 240810054
                @SerializedName("type")
                val type: String, // private
                @SerializedName("username")
                val username: String // theapache64
        )

        data class Entity(
                @SerializedName("length")
                val length: Int, // 5
                @SerializedName("offset")
                val offset: Int, // 0
                @SerializedName("type")
                val type: String // bot_command
        )

        data class From(
                @SerializedName("first_name")
                val firstName: String, // theapache64
                @SerializedName("id")
                val id: Int, // 240810054
                @SerializedName("is_bot")
                val isBot: Boolean, // false
                @SerializedName("language_code")
                val languageCode: String, // en
                @SerializedName("username")
                val username: String // theapache64
        )
    }
}