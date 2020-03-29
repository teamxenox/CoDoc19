package com.teamxenox.codoc19.core.base

interface BotAgent {
    fun handle(data: Any)
    fun startQuiz()
    fun startTest()
    fun sendStats()
}