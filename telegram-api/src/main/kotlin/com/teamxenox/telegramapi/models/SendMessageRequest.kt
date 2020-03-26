package com.teamxenox.telegramapi.models

import com.fasterxml.jackson.annotation.JsonProperty


data class SendMessageRequest(
        @field:JsonProperty("chat_id")
        val chatId: Int, // to
        @JsonProperty("text")
        val text: String, // This is some message
        @JsonProperty("disable_web_page_preview")
        val isDisableWebPagePreview: Boolean?,
        @JsonProperty("parse_mode")
        val parseMode: String?,
        @JsonProperty("reply_to_message_id")
        val replyMsgId: Long?,
        @JsonProperty("reply_markup")
        val replyMarkup: ReplyMarkup?
) {
    data class ReplyMarkup(
            @JsonProperty("inline_keyboard")
            val inlineKeyboard: List<List<InlineButton>>
    )

    data class InlineButton(
            @JsonProperty("text")
            val text: String, // ✅ Relevant
            @JsonProperty("callback_data")
            val callbackData: String // r123
    ) {
        class ByteOverflowException(message: String?) : Throwable(message)

        init {
            val byteSize = callbackData.toByteArray().size
            if (byteSize > 64) {
                throw ByteOverflowException(
                        "Callback data exceeded"
                )
            }
        }
    }
}