package com.teamxenox.codoc19.core.features.quiz

import com.teamxenox.codoc19.core.base.FeatureProxy
import com.teamxenox.telegramapi.Telegram

class QuizBoss(telegramApi: Telegram, chatId: Long, messageId: Long) : FeatureProxy(telegramApi, chatId, messageId) {

    override fun handle(jsonString: String) {
        TODO("Not yet implemented")
    }

}