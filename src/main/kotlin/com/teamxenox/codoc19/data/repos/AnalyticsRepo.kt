package com.teamxenox.codoc19.data.repos

import com.teamxenox.codoc19.data.entities.Analytics
import org.springframework.data.repository.CrudRepository

interface AnalyticsRepo : CrudRepository<Analytics, Int> {

}