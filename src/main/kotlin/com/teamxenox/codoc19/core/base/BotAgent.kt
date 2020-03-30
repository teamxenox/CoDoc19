package com.teamxenox.codoc19.core.base

import com.teamxenox.codoc19.data.repos.UserRepo

abstract class BotAgent(userRepo: UserRepo) {
    abstract fun handle(data: Any)
    abstract fun startQuiz()
    abstract fun startTest()
    abstract fun sendGlobalStats()
    abstract fun sendCountryStats(country: String)
}