package com.teamxenox.codoc19.controllers

import com.teamxenox.bootzan.models.base.BaseResponse
import com.teamxenox.codoc19.core.BotsManager
import com.teamxenox.telegramapi.models.Update
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class GetResponseController {

    companion object {
        private const val BOT_TYPE_TELEGRAM = "telegram"
    }

    @PostMapping("/get_response/{agentKey}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun getResponse(@PathVariable agentKey: String, @RequestBody update: Any): BaseResponse<Void> {
        val agent = BotsManager.getAgentOrThrow(agentKey)
        agent.handle(update)
        return BaseResponse(null, false, -1, "OK")
    }
}