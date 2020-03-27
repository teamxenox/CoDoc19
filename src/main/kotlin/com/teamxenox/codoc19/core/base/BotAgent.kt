package com.teamxenox.codoc19.core.base

interface BotAgent {
    fun handle(data: Any)
    fun runQuiz()
    fun runTest()
    fun sendUpdate()
}