package com.teamxenox.codoc19.core.base

interface BotAgent {
    fun handle(data: Any)
    fun startQuiz()
    fun startTest()
    fun sendGlobalStats()
    fun sendCountryStats(country : String)
}