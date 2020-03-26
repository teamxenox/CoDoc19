package com.teamxenox.codoc19

import com.teamxenox.codoc19.core.SecretConstants
import com.teamxenox.telegramapi.TelegramAPI
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Codoc19Application

fun main(args: Array<String>) {
    runApplication<Codoc19Application>(*args)

    val telegramApi = TelegramAPI(SecretConstants.TELEGRAM_ACTIVE_BOT_TOKEN)
}
