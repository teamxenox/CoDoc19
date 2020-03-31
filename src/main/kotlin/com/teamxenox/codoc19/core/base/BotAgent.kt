package com.teamxenox.codoc19.core.base

import com.teamxenox.codoc19.data.repos.AnalyticsRepo
import com.teamxenox.codoc19.data.repos.UserRepo
import com.teamxenox.codoc19.models.Country

abstract class BotAgent(
        userRepo: UserRepo,
        analyticsRepo: AnalyticsRepo
) {
    abstract fun handle(data: Any)
    abstract fun startQuiz()
    abstract fun startTest()
    abstract fun sendGlobalStats()
    abstract fun sendCountryStats(country: Country)
}