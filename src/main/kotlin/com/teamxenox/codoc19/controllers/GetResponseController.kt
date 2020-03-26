package com.teamxenox.codoc19.controllers

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus

@Controller
class GetResponseController {

    companion object {
        private const val BOT_TYPE_TELEGRAM = "telegram"
    }

    @PostMapping("/get_response/{botType}")
    @ResponseStatus(HttpStatus.OK)
    fun getResponse(@PathVariable botType: String) {
        println("Hit! $botType")
    }
}