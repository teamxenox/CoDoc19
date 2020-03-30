package com.teamxenox.codoc19

import com.teamxenox.codoc19.core.SecretConstants
import com.teamxenox.telegramapi.Telegram
import com.winterbe.expekt.should
import org.junit.jupiter.api.Test
import java.io.File

class TelegramApiTest {

    @Test
    fun testSendPhotoFileSuccess() {
        val telegramApi = Telegram(SecretConstants.TELEGRAM_ACTIVE_BOT_TOKEN)
        val chatId = "240810054"
        val file = File("charts/chart.png")
        val resp = telegramApi.sendPhotoFile(chatId, file)
        resp.code().should.equal(200)
        println(resp.body())
    }
}