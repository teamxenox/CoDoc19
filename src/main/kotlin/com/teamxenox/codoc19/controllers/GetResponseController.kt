package com.teamxenox.codoc19.controllers

import com.teamxenox.bootzan.models.base.BaseResponse
import com.teamxenox.codoc19.core.BotsManager
import com.teamxenox.codoc19.data.repos.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class GetResponseController {


    @Autowired
    lateinit var userRepo: UserRepo

    @PostMapping("/get_response/{agentKey}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun getResponse(@PathVariable agentKey: String, @RequestBody update: Any): String {
        println("---------------------------")
        println("Hit!!")
        val bm = BotsManager(userRepo)
        val agent = bm.getAgentOrThrow(agentKey)
        agent.handle(update)
        return "ok"
    }
}